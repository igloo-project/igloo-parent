package fr.openwide.core.wicket.more.notification.service;

import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT;

import java.util.Locale;
import java.util.concurrent.Callable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.ThreadContext;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.protocol.http.mock.MockHttpServletResponse;
import org.apache.wicket.protocol.http.mock.MockHttpSession;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.spring.property.service.IPropertyService;

public class WicketContextExecutorImpl implements IWicketContextExecutor {
	
	@Autowired
	private IPropertyService propertyService;
	
	private final String defaultApplicationName;
	
	/**
	 * @param defaultApplicationName The name given to the wicket application to be used by this executor when none is provided by clients.
	 */
	public WicketContextExecutorImpl(String defaultApplicationName) {
		this.defaultApplicationName = defaultApplicationName;
	}
	
	@Override
	public <T> T runWithContext(Callable<T> callable) throws Exception {
		return runWithContext(defaultApplicationName, callable);
	}
	
	@Override
	public <T> T runWithContext(String applicationName, Callable<T> callable) throws Exception {
		RequestCycleThreadAttachmentStatus requestCycleStatus = null;
		
		try {
			requestCycleStatus = attachRequestCycleIfNeeded(applicationName);
			
			return callable.call();
		} finally {
			if (requestCycleStatus != null) {
				detachRequestCycleIfNeeded(requestCycleStatus);
			}
		}
	}

	@Override
	public <T> T runWithContext(Callable<T> callable, Locale locale) throws Exception {
		return runWithContext(defaultApplicationName, callable, locale);
	}

	@Override
	public <T> T runWithContext(String applicationName, Callable<T> callable, Locale locale) throws Exception {
		RequestCycleThreadAttachmentStatus requestCycleStatus = null;
		
		try {
			requestCycleStatus = attachRequestCycleIfNeeded(defaultApplicationName);
			
			Session session = Session.get();
			Locale oldLocale = session.getLocale();
			try {
				session.setLocale(propertyService.toAvailableLocale(locale));
				return callable.call();
			} finally {
				session.setLocale(oldLocale);
			}
		} finally {
			if (requestCycleStatus != null) {
				detachRequestCycleIfNeeded(requestCycleStatus);
			}
		}
	}
	
	private void detachRequestCycleIfNeeded(RequestCycleThreadAttachmentStatus status) {
		if (RequestCycleThreadAttachmentStatus.TEMPORARY.equals(status)) {
			ThreadContext.detach();
		}
	}
	
	private RequestCycleThreadAttachmentStatus attachRequestCycleIfNeeded(String applicationName) {
		WebApplication webApplication = retrieveWebApplication(applicationName);
		return attachRequestCycleIfNeeded(webApplication);
	}
	
	private WebApplication retrieveWebApplication(String applicationName) {
		WebApplication webApplication;
		if (WebApplication.exists()) {
			webApplication = WebApplication.get();
		} else {
			Application application = WebApplication.get(applicationName);
			if (!(application instanceof WebApplication)) {
				throw new IllegalStateException("Application is not a WebApplication");
			}
			webApplication = (WebApplication) application;
			ThreadContext.setApplication(webApplication);
		}
		return webApplication;
	}
	
	private RequestCycleThreadAttachmentStatus attachRequestCycleIfNeeded(WebApplication application) {
		if (RequestCycle.get() != null) {
			return RequestCycleThreadAttachmentStatus.PERMANENT;
		}
		final ServletContext context = application.getServletContext();
		
		final HttpSession newHttpSession = new MockHttpSession(context);
		final MockHttpServletRequest servletRequest = new ContextConfiguredMockHttpServletRequest(application, newHttpSession, context);
		final MockHttpServletResponse servletResponse = new MockHttpServletResponse(servletRequest);
		servletRequest.initialize();
		servletResponse.initialize();
		
		final ServletWebRequest webRequest = new ServletWebRequest(servletRequest, servletRequest.getFilterPrefix());
		final WebResponse webResponse = new BufferedWebResponse(new ServletWebResponse(webRequest, servletResponse));
		
		RequestCycle requestCycle = application.createRequestCycle(webRequest, webResponse);
		ThreadContext.setRequestCycle(requestCycle);
		
		return RequestCycleThreadAttachmentStatus.TEMPORARY;
	}
	
	private enum RequestCycleThreadAttachmentStatus {
		PERMANENT,
		TEMPORARY
	}
	
	private class ContextConfiguredMockHttpServletRequest extends MockHttpServletRequest {

		public ContextConfiguredMockHttpServletRequest(Application application, HttpSession session,
				ServletContext context) {
			super(application, session, context);
		}
		
		@Override
		public String getServerName() {
			return propertyService.get(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME);
		}
		
		@Override
		public int getServerPort() {
			return propertyService.get(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT);
		}

		@Override
		public String getScheme() {
			return propertyService.get(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME);
		}
		
		@Override
		public String getContextPath() {
			return getServletContext().getContextPath();
		}
		
		@Override
		public String getServletPath() {
			return "/";
		}
	}

}