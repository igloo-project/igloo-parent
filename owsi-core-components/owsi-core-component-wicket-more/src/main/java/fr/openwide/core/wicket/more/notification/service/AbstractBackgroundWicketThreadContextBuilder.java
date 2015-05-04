package fr.openwide.core.wicket.more.notification.service;

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

import fr.openwide.core.context.IContextualService;
import fr.openwide.core.spring.config.CoreConfigurer;

public abstract class AbstractBackgroundWicketThreadContextBuilder implements IContextualService {
	
	@Autowired
	protected CoreConfigurer configurer;
	
	protected abstract String getApplicationName();
	
	@Override
	public <T> T runWithContext(Callable<T> callable) throws Exception {
		return runWithContext(callable, null);
	}
	
	protected <T> T runWithContext(Callable<T> callable, Locale locale) throws Exception {
		RequestCycleThreadAttachmentStatus requestCycleStatus = null;
		
		try {
			requestCycleStatus = attachRequestCycleIfNeeded(getApplicationName());
			
			Session session = Session.get();
			
			Locale oldLocale = session.getLocale();
			try {
				if (locale != null) {
					session.setLocale(configurer.toAvailableLocale(locale));
				}
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
	
	protected void detachRequestCycleIfNeeded(RequestCycleThreadAttachmentStatus status) {
		if (RequestCycleThreadAttachmentStatus.TEMPORARY.equals(status)) {
			ThreadContext.detach();
		}
	}
	
	protected RequestCycleThreadAttachmentStatus attachRequestCycleIfNeeded(String applicationName) {
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
	
	protected enum RequestCycleThreadAttachmentStatus {
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
			return configurer.getWicketBackgroundRequestCycleBuilderUrlServerName();
		}
		
		@Override
		public int getServerPort() {
			return configurer.getWicketBackgroundRequestCycleBuilderUrlServerPort();
		}

		@Override
		public String getScheme() {
			return configurer.getWicketBackgroundRequestCycleBuilderUrlScheme();
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