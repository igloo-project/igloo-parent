package org.iglooproject.wicket.more.request.cycle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.springframework.security.web.savedrequest.SavedRequest;

public final class RequestCycleUtils {
	
	private static final String QUERY_STRING_SEPARATOR = "?";
	
	public static final String SPRING_SECURITY_SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";
	
	public static String getCurrentRequestUrl() {
		Request request = RequestCycle.get().getRequest();
		if (!(request instanceof ServletWebRequest)) {
			throw new IllegalStateException("Cannot be used in a non servlet environment");
		}
		
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		StringBuffer currentUrl = servletWebRequest.getContainerRequest().getRequestURL();
		String queryString = servletWebRequest.getContainerRequest().getQueryString();
		if (queryString != null) {
			currentUrl.append(QUERY_STRING_SEPARATOR).append(queryString);
		}
		
		return currentUrl.toString();
	}
	
	public static HttpServletRequest getCurrentContainerRequest() {
		Request request = RequestCycle.get().getRequest();
		if (!(request instanceof ServletWebRequest)) {
			throw new IllegalStateException("Cannot be used in a non servlet environment");
		}
		
		return ((ServletWebRequest) request).getContainerRequest();
	}
	
	public static HttpServletResponse getCurrentContainerResponse() {
		// ici, on ne peut pas utiliser le même mécanisme qu'au dessus car la response peut aussi être
		// une HeaderBufferingWebResponse et comme la classe est package protected, on est coincé...
		Object containerResponse = RequestCycle.get().getResponse().getContainerResponse();
		
		if (!(containerResponse instanceof HttpServletResponse)) {
			throw new IllegalStateException("Cannot be used in a non servlet environment");
		}
		
		return ((HttpServletResponse) containerResponse);
	}
	
	public static String getSpringSecuritySavedRequest() {
		String redirectUrl = null;
		
		Object savedRequest = RequestCycleUtils.getCurrentContainerRequest().getSession().getAttribute(SPRING_SECURITY_SAVED_REQUEST);
		if (savedRequest instanceof SavedRequest) {
			redirectUrl = ((SavedRequest) savedRequest).getRedirectUrl();
		}
		RequestCycleUtils.getCurrentContainerRequest().getSession().removeAttribute(SPRING_SECURITY_SAVED_REQUEST);
		
		return redirectUrl;
	}

	private RequestCycleUtils() {
	}

}
