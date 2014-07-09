package fr.openwide.core.jpa.more.business.link.service;

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
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkErrorType;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkStatus;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;
import fr.openwide.core.spring.config.CoreConfigurer;

@Service("externalLinkCheckerService")
public class ExternalLinkCheckerServiceImpl implements IExternalLinkCheckerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExternalLinkCheckerServiceImpl.class);

	@Autowired
	private IExternalLinkWrapperService externalLinkWrapperService;
	
	@Autowired
	private ConfigurableApplicationContext applicationContext;
	
	@Autowired
	private CoreConfigurer configurer;
	
	private CloseableHttpClient httpClient = null;
	
	/**
	 * we can put here some URLs known to fail. Otherwise, it's better to do it directly in the application.
	 */
	private List<Pattern> ignorePatterns = Lists.newArrayList(
			//Pattern.compile("^http://translate.googleusercontent.com/.*")
	);
	
	@PostConstruct
	private void initialize() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setMaxRedirects(configurer.getExternalLinkCheckerMaxRedirects())
				.setSocketTimeout(configurer.getExternalLinkCheckerTimeout())
				.setConnectionRequestTimeout(configurer.getExternalLinkCheckerTimeout())
				.setConnectTimeout(configurer.getExternalLinkCheckerTimeout())
				.setStaleConnectionCheckEnabled(true)
				.setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY) // contournement d'un bug JVM. Cf https://code.google.com/p/crawler4j/issues/detail?id=136
				.build();
		
		httpClient = HttpClientBuilder.create()
				.setUserAgent(configurer.getExternalLinkCheckerUserAgent())
				.setDefaultRequestConfig(requestConfig)
				.setDefaultHeaders(Lists.newArrayList(
						new BasicHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE),
						new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
						new BasicHeader("Accept-Language", "fr,en;q=0.8,fr-fr;q=0.6,en-us;q=0.4,en-gb;q=0.2")
				))
				.build();
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
		List<ExternalLinkWrapper> links = externalLinkWrapperService.listNextCheckingBatch(configurer.getExternalLinkCheckerBatchSize());
		
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
		
		// Check the URL and update the links
		try {
			// We try a HEAD request
			httpStatus = sendRequest(new HttpHead(uri));
			if (httpStatus != null && httpStatus.getStatusCode() != HttpStatus.SC_OK) {
				// If the result of the HEAD request is not OK, we try a GET request
				// Using HttpStatus.SC_METHOD_NOT_ALLOWED looked like a clever trick but a lot of sites return
				// 400 or 500 errors for HEAD requests
				httpStatus = sendRequest(new HttpGet(uri));
			}
			if (httpStatus == null) {
				errorType = ExternalLinkErrorType.UNKNOWN_HTTPCLIENT_ERROR;
			} else if (httpStatus.getStatusCode() != HttpStatus.SC_OK) {
				errorType = ExternalLinkErrorType.HTTP;
			}
		} catch (IllegalArgumentException e) {
			errorType = ExternalLinkErrorType.INVALID_IDN;
		} catch (SocketTimeoutException e) {
			errorType = ExternalLinkErrorType.TIMEOUT;
		} catch (IOException e) {
			errorType = ExternalLinkErrorType.IO;
		}
		
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
			} catch (Exception e) {
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
			externalLinkWrapperService.update(link);
		}
	}
	
	private void markAsOfflineOrDead(Collection<ExternalLinkWrapper> links, final ExternalLinkErrorType errorType, final StatusLine status) throws ServiceException, SecurityServiceException {
		Date checkDate = new Date();
		for (ExternalLinkWrapper link : links) {
			link.setLastCheckDate(checkDate);
			link.setLastErrorType(errorType);
			link.setConsecutiveFailures(link.getConsecutiveFailures() + 1);
			
			if (status != null) {
				link.setLastStatusCode(status.getStatusCode());
			} else {
				link.setLastStatusCode(null);
			}
			
			if (link.getConsecutiveFailures() >= configurer.getExternalLinkCheckerRetryAttemptsLimit()) {
				link.setStatus(ExternalLinkStatus.DEAD_LINK);
			} else {
				link.setStatus(ExternalLinkStatus.OFFLINE);
			}
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
	
	private void runTasksInParallel(Collection<? extends Callable<Void>> tasks, long timeout, TimeUnit timeoutUnit) throws ServiceException {
		final int threadPoolSize = configurer.getExternalLinkCheckerThreadPoolSize();
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
		} catch (Exception e) {
			throw new ServiceException("Interrupted request", e);
		} finally {
			try {
				executor.shutdown();
			} catch (Exception e) {
				LOGGER.warn("An error occurred while shutting down threads", e);
			}
		}
	}
	
	public void addIgnorePattern(Pattern ignorePattern) {
		this.ignorePatterns.add(ignorePattern);
	}
}
