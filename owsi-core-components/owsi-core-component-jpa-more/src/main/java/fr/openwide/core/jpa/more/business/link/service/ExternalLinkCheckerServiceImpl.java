package fr.openwide.core.jpa.more.business.link.service;

import io.mola.galimatias.GalimatiasParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
				.setDefaultHeaders(Lists.newArrayList(new BasicHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE)))
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
		Multimap<String, ExternalLinkWrapper> links = externalLinkWrapperService.listNextCheckingBatch(configurer.getExternalLinkCheckerBatchSize());
		
		runTasksInParallel(createTasksByDomain(links), 10, TimeUnit.HOURS);
	}
	
	@Override
	public void checkLink(final ExternalLinkWrapper link) throws ServiceException, SecurityServiceException {
		checkLinksWithSameUrl(link.getUrl(), ImmutableList.of(link));
	}
	
	@Override
	public void checkLinksWithSameUrl(String urlString, Collection<ExternalLinkWrapper> links) throws ServiceException, SecurityServiceException {
		URI uri;
		StatusLine status = null;
		ExternalLinkErrorType errorType = null;
		
		// Special casing the ignore patterns
		for (Pattern pattern : ignorePatterns) {
			if (pattern.matcher(urlString).matches()) {
				Date checkDate = new Date();
				for (ExternalLinkWrapper link : links) {
					link.setLastCheckDate(checkDate);
					link.setConsecutiveFailures(0);
					link.setStatus(ExternalLinkStatus.IGNORED);
					externalLinkWrapperService.update(link);
				}
				return;
			}
		}
		
		// Check the URL and update the links
		try {
			uri = getURI(urlString);
			
			// We try a HEAD request
			status = sendRequest(uri, true);
			if (status != null && status.getStatusCode() != HttpStatus.SC_OK) {
				// If the result of the HEAD request is not OK, we try a GET request
				// Using HttpStatus.SC_METHOD_NOT_ALLOWED looked like a clever trick but a lot of sites return
				// 400 or 500 errors for HEAD requests
				status = sendRequest(uri, false);
			}
			if (status == null) {
				errorType = ExternalLinkErrorType.UNKNOWN_HTTPCLIENT_ERROR;
			} else if (status.getStatusCode() != HttpStatus.SC_OK) {
				errorType = ExternalLinkErrorType.HTTP;
			}
		} catch (MalformedURLException e) {
			errorType = ExternalLinkErrorType.MALFORMED_URL;
		} catch (IllegalArgumentException e) {
			errorType = ExternalLinkErrorType.INVALID_IDN;
		} catch (URISyntaxException e) {
			errorType = ExternalLinkErrorType.URI_SYNTAX;
		}
		
		Date checkDate = new Date();
		for (ExternalLinkWrapper link : links) {
			link.setLastCheckDate(checkDate);
			if (errorType == null) {
				onSuccessfulCheck(link);
			} else {
				onCheckFailure(link, errorType, status);
			}
			externalLinkWrapperService.update(link);
		}
	}
	
	// Private methods
	
	private Collection<Callable<Void>> createTasksByDomain(Multimap<String, ExternalLinkWrapper> links) throws ServiceException, SecurityServiceException {
		Collection<Callable<Void>> tasks = Lists.newArrayList();

		Map<String, Multimap<String, Long>> domainToUrlToIds = Maps.newLinkedHashMap();
		
		for (Entry<String, Collection<ExternalLinkWrapper>> entry : links.asMap().entrySet()) {
			String url = entry.getKey();
			String domain = getDomain(url);
			if (domain == null) {
				markAsInvalid(entry.getValue());
			} else {
				Multimap<String, Long> urlToIds = domainToUrlToIds.get(domain);
				if (urlToIds == null) {
					urlToIds = LinkedListMultimap.create();
					domainToUrlToIds.put(domain, urlToIds);
				}
				for (ExternalLinkWrapper link : entry.getValue()) {
					urlToIds.put(url, link.getId());
				}
			}
		}
		
		for (Multimap<String, Long> urlToIds : domainToUrlToIds.values()) {
			tasks.add(new ExternalLinkCheckByDomainTask(applicationContext, urlToIds.asMap()));
		}
		
		return tasks;
	}
	
	private String getDomain(String string) {
		try {
			io.mola.galimatias.URL url = io.mola.galimatias.URL.parse(string);
			return url.host().toHumanString();
		} catch (Exception e) {
			return null;
		}
	}
	
	private URI getURI(String urlString) throws URISyntaxException, MalformedURLException, IllegalArgumentException {
		try {
			io.mola.galimatias.URL url = io.mola.galimatias.URL.parse(urlString);
			return url.toJavaURI();
		} catch (GalimatiasParseException ex) {
			throw new URISyntaxException(urlString, ex.getMessage());
		}
	}

	private void markAsInvalid(Collection<ExternalLinkWrapper> linkWrappers) throws ServiceException, SecurityServiceException {
		Date checkDate = new Date();
		for (ExternalLinkWrapper link : linkWrappers) {
			link.setStatus(ExternalLinkStatus.DEAD_LINK);
			link.setLastErrorType(ExternalLinkErrorType.URI_SYNTAX);
			link.setLastCheckDate(checkDate);
			externalLinkWrapperService.update(link);
		}
	}
	
	private void onSuccessfulCheck(final ExternalLinkWrapper link) {
		link.setConsecutiveFailures(0);
		link.setStatus(ExternalLinkStatus.ONLINE);
		link.setLastStatusCode(HttpStatus.SC_OK);
	}
	
	private void onCheckFailure(final ExternalLinkWrapper link, final ExternalLinkErrorType errorType, final StatusLine status) {
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
	}
	
	private StatusLine sendRequest(final URI url, boolean httpHead) {
		HttpRequestBase method = null;
		CloseableHttpResponse response = null;
		
		try {
			method = (httpHead ? new HttpHead(url) : new HttpGet(url));
			
			response = httpClient.execute(method);
			return response.getStatusLine();
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder()
				.append("An error occurred while performing a ")
				.append(httpHead ? "HEAD" : "GET")
				.append(" request on ")
				.append(url);
			LOGGER.debug(sb.toString(), e);
		} finally {
			if (method != null) {
				method.reset();
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					LOGGER.error("Unable to close the HTTP response", e);
				}
			}
		}
		return null;
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
