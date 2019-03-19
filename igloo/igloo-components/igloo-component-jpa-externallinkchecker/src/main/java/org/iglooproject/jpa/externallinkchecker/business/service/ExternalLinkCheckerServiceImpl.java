package org.iglooproject.jpa.externallinkchecker.business.service;

import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.BATCH_SIZE;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.MAX_REDIRECTS;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.MIN_DELAY_BETWEEN_TWO_CHECKS_IN_DAYS;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.RETRY_ATTEMPTS_NUMBER;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.THREAD_POOL_SIZE;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.TIMEOUT;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.USER_AGENT;
import io.mola.galimatias.GalimatiasParseException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkErrorType;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkStatus;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkWrapper;
import org.iglooproject.spring.property.service.IPropertyService;

@Service("externalLinkCheckerService")
public class ExternalLinkCheckerServiceImpl implements IExternalLinkCheckerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExternalLinkCheckerServiceImpl.class);

	@Autowired
	private IExternalLinkWrapperService externalLinkWrapperService;
	
	@Autowired
	private ConfigurableApplicationContext applicationContext;
	
	@Autowired
	private IPropertyService propertyService;
	
	private CloseableHttpClient httpClient = null;
	
	/**
	 * we can put here some URLs known to fail. Otherwise, it's better to do it directly in the application.
	 */
	private List<Pattern> ignorePatterns = Lists.newArrayList(
			//Pattern.compile("^http://translate.googleusercontent.com/.*")
	);
	
	private int batchSize;
	
	private int minDelayBetweenTwoChecks;
	
	@PostConstruct
	private void initialize() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setMaxRedirects(propertyService.get(MAX_REDIRECTS))
				.setSocketTimeout(propertyService.get(TIMEOUT))
				.setConnectionRequestTimeout(propertyService.get(TIMEOUT))
				.setConnectTimeout(propertyService.get(TIMEOUT))
				.setStaleConnectionCheckEnabled(true) // waiting for this to be resolved: https://issues.apache.org/jira/browse/HTTPCLIENT-1656
				.build();
		
		httpClient = HttpClientBuilder.create()
				.setUserAgent(propertyService.get(USER_AGENT))
				.setDefaultRequestConfig(requestConfig)
				.setDefaultHeaders(Lists.newArrayList(
						new BasicHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE),
						new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
						new BasicHeader("Accept-Language", "fr,en;q=0.8,fr-fr;q=0.6,en-us;q=0.4,en-gb;q=0.2")
				))
				.build();
		
		batchSize = propertyService.get(BATCH_SIZE);
		minDelayBetweenTwoChecks = propertyService.get(MIN_DELAY_BETWEEN_TWO_CHECKS_IN_DAYS);
	}
	
	@PreDestroy
	private void destroy() {
		if (httpClient != null) {
			try {
				httpClient.close();
			} catch (IOException e) {
				LOGGER.error("Unable to close the HTTP client", e);
			}
		}
	}
	
	// Public methods
	
	@Override
	public void checkBatch() throws ServiceException, SecurityServiceException {
		List<ExternalLinkWrapper> links = externalLinkWrapperService.listNextCheckingBatch(
				batchSize, minDelayBetweenTwoChecks);
		
		runTasksInParallel(createTasksByDomain(links), 10, TimeUnit.HOURS);
	}
	
	@Override
	public void checkLink(final ExternalLinkWrapper link) throws ServiceException, SecurityServiceException {
		try {
			io.mola.galimatias.URL url = io.mola.galimatias.URL.parse(link.getUrl());
			checkLinksWithSameUrl(url, ImmutableList.of(link));
		} catch (GalimatiasParseException e) {
			markAsInvalid(link);
		}
	}
	
	@Override
	public void checkLinksWithSameUrl(io.mola.galimatias.URL url, Collection<ExternalLinkWrapper> links) throws ServiceException, SecurityServiceException {
		StatusLine httpStatus = null;
		ExternalLinkErrorType errorType = null;
		
		// Special casing the ignore patterns
		for (Pattern pattern : ignorePatterns) {
			if (pattern.matcher(url.toHumanString()).matches()) {
				markAsIgnored(links);
				return;
			}
		}
		
		URI uri;
		try {
			uri = url.toJavaURI();
		} catch (URISyntaxException e) {
			// java.net.URI is buggy and doesn't support domain names with underscores. As galimatias already check
			// the URL format, if we are here, it's probably because java.net.URI doesn't handle this case very well
			// so we simply ignore the link instead of marking it as dead.
			// see http://bugs.java.com/view_bug.do?bug_id=6587184 and https://issues.apache.org/jira/browse/HTTPCLIENT-911
			markAsIgnored(links);
			return;
		}
		
		boolean headIfTrueElseGet = true;
		boolean retry = false;
		int numAttempts = 0;
		do {
			// Check the URL and update the links
			try {
				// If we are retrying, be sure to have these variables at initial values
				errorType = null;
				httpStatus = null;
				
				// We try a HEAD request
				if (headIfTrueElseGet) {
					httpStatus = sendRequest(new HttpHead(uri));
					
					if (httpStatus != null && httpStatus.getStatusCode() != HttpStatus.SC_OK) {
						// If the result of the HEAD request is not OK, we try a GET request
						// Using HttpStatus.SC_METHOD_NOT_ALLOWED looked like a clever trick but a lot of sites return
						// 400 or 500 errors for HEAD requests
						httpStatus = sendRequest(new HttpGet(uri));
					}
				} else {
					// If HEAD request didn't work, we try a GET request
					httpStatus = sendRequest(new HttpGet(uri));
				}
				if (httpStatus == null) {
					errorType = ExternalLinkErrorType.UNKNOWN_HTTPCLIENT_ERROR;
				} else if (httpStatus.getStatusCode() != HttpStatus.SC_OK) {
					errorType = ExternalLinkErrorType.HTTP;
				}
			} catch (IllegalArgumentException e) {
				errorType = ExternalLinkErrorType.INVALID_IDN;
				LOGGER.debug("IllegalArgumentException while checking external link (" + uri.toString() + ").", e);
			} catch (SocketTimeoutException | ClientProtocolException e) {
				// If HEAD request failed with socket timeout, let's try with GET request
				if (headIfTrueElseGet) {
					// Sample url : http://myspace.com/ (same results with https)
					// If we try with curl or wget, it seems to give us the same waiting result.
					// curl --head 'http://myspace.com/'
					// wget --method=HEAD 'http://myspace.com/'
					headIfTrueElseGet = false;
					retry = true;
					LOGGER.warn("HEAD request on external link (" + uri.toString() + ") resulted in a timeout"
							+ " or another weird exception, we will try a GET request.", e);
				} else {
					errorType = ExternalLinkErrorType.fromException(e);
				}
			} catch (SSLHandshakeException e) {
				// certificate not supported by Java: we ignore the links
				markAsIgnored(links);
				return;
			} catch (ConnectionPoolTimeoutException e) {
				// If we have connection pool problem, the problem is at our side, we don't affect links status.
				LOGGER.debug("ConnectionPoolTimeoutException while checking external link (" + uri.toString() + ").", e);
				return;
			} catch (IOException e) {
				errorType = ExternalLinkErrorType.fromException(e);
			}
			numAttempts++;
			
			// If retry is needed, go back to start of do...while
			// We never try more than 2 attempts
		} while (numAttempts < 2 && retry);
		
		if (errorType == null) {
			markAsOnline(links);
		} else {
			markAsOfflineOrDead(links, errorType, httpStatus);
		}
	}
	
	// Private methods
	
	private Collection<Callable<Void>> createTasksByDomain(List<ExternalLinkWrapper> links) throws ServiceException, SecurityServiceException {
		Collection<Callable<Void>> tasks = Lists.newArrayList();
		
		Map<String, Multimap<io.mola.galimatias.URL, Long>> domainToUrlToIds = Maps.newLinkedHashMap();
		
		for (ExternalLinkWrapper link : links) {
			try {
				// there's no need to normalize the URL further as galimatias already normalizes the host and it's the
				// only part we can normalize.
				io.mola.galimatias.URL url = io.mola.galimatias.URL.parse(link.getUrl());
				String domain = url.host().toHumanString();
				
				Multimap<io.mola.galimatias.URL, Long> urlToIds = domainToUrlToIds.get(domain);
				if (urlToIds == null) {
					urlToIds = LinkedListMultimap.create();
					domainToUrlToIds.put(domain, urlToIds);
				}
				urlToIds.put(url, link.getId());
			} catch (RuntimeException | GalimatiasParseException e) {
				// if we cannot parse the URI, there's no need to go further, we mark it as invalid and we ignore it
				markAsInvalid(link);
			}
		}
		
		for (Multimap<io.mola.galimatias.URL, Long> urlToIds : domainToUrlToIds.values()) {
			tasks.add(new ExternalLinkCheckByDomainTask(applicationContext, urlToIds.asMap()));
		}
		
		return tasks;
	}

	private void markAsInvalid(ExternalLinkWrapper link) throws ServiceException, SecurityServiceException {
		link.setLastCheckDate(new Date());
		link.setStatus(ExternalLinkStatus.DEAD_LINK);
		link.setLastErrorType(ExternalLinkErrorType.URI_SYNTAX);
		externalLinkWrapperService.update(link);
	}
	
	private void markAsIgnored(Collection<ExternalLinkWrapper> links) throws ServiceException, SecurityServiceException {
		Date checkDate = new Date();
		for (ExternalLinkWrapper link : links) {
			link.setLastCheckDate(checkDate);
			link.setConsecutiveFailures(0);
			link.setStatus(ExternalLinkStatus.IGNORED);
			externalLinkWrapperService.update(link);
		}
	}
	
	private void markAsOnline(Collection<ExternalLinkWrapper> links) throws ServiceException, SecurityServiceException {
		Date checkDate = new Date();
		for (ExternalLinkWrapper link : links) {
			link.setLastCheckDate(checkDate);
			link.setConsecutiveFailures(0);
			link.setStatus(ExternalLinkStatus.ONLINE);
			link.setLastStatusCode(HttpStatus.SC_OK);
			link.setFailureAudit(null);
			externalLinkWrapperService.update(link);
		}
	}
	
	private void markAsOfflineOrDead(Collection<ExternalLinkWrapper> links, final ExternalLinkErrorType errorType, final StatusLine httpStatus) throws ServiceException, SecurityServiceException {
		Date checkDate = new Date();
		for (ExternalLinkWrapper link : links) {
			link.setLastCheckDate(checkDate);
			link.setLastErrorType(errorType);
			int consecutiveFailures = link.getConsecutiveFailures() + 1;
			link.setConsecutiveFailures(consecutiveFailures);
			
			Integer statusCode;
			if (httpStatus != null) {
				statusCode = httpStatus.getStatusCode();
			} else {
				statusCode = null;
			}
			link.setLastStatusCode(statusCode);
			
			ExternalLinkStatus status;
			if (link.getConsecutiveFailures() >= propertyService.get(RETRY_ATTEMPTS_NUMBER)) {
				status = ExternalLinkStatus.DEAD_LINK;
			} else {
				status = ExternalLinkStatus.OFFLINE;
			}
			link.setStatus(status);
			
			// Failure audit
			StringBuilder failureAuditBuilder = new StringBuilder();
			String failureAudit = link.getFailureAudit();
			if (StringUtils.hasText(failureAudit)) {
				failureAuditBuilder.append(failureAudit).append("\n");
			}
			
			failureAuditBuilder.append(
					new ToStringBuilder(null, ToStringStyle.DEFAULT_STYLE)
							.append("checkDate", checkDate)
							.append("errorType", errorType)
							.append("consecutiveFailures", consecutiveFailures)
							.append("statusCode", statusCode)
							.append("status", status)
							.build()
			);
			link.setFailureAudit(failureAuditBuilder.toString());
			
			externalLinkWrapperService.update(link);
		}
	}
	
	private StatusLine sendRequest(HttpRequestBase request) throws IOException {
		CloseableHttpResponse response = null;
		
		try {
			response = httpClient.execute(request);
			return response.getStatusLine();
		} finally {
			if (request != null) {
				request.reset();
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					LOGGER.error("Unable to close the HTTP response", e);
				}
			}
		}
	}
	
	private void runTasksInParallel(Collection<? extends Callable<Void>> tasks, long timeout, TimeUnit timeoutUnit)
			throws ServiceException {
		final int threadPoolSize = propertyService.get(THREAD_POOL_SIZE);
		final ThreadPoolExecutor executor = new ThreadPoolExecutor(
				threadPoolSize, threadPoolSize,
				100, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		
		executor.prestartAllCoreThreads();
		
		try {
			List<Future<Void>> futures = executor.invokeAll(tasks, timeout, timeoutUnit);
			for (Future<Void> future : futures) {
				future.get(); // Check that no error has occurred
			}
		} catch (RuntimeException | InterruptedException | ExecutionException e) {
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			throw new ServiceException("Interrupted request", e);
		} finally {
			try {
				executor.shutdown();
			} catch (RuntimeException e) {
				LOGGER.warn("An error occurred while shutting down threads", e);
			}
		}
	}
	
	@Override
	public void addIgnorePattern(Pattern ignorePattern) {
		this.ignorePatterns.add(ignorePattern);
	}
}
