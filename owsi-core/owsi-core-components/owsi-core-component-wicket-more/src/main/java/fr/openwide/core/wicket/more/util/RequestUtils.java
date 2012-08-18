package fr.openwide.core.wicket.more.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;

public final class RequestUtils {
	
	private static final String QUERY_STRING_SEPARATOR = "?";
	
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
		Response response = RequestCycle.get().getResponse();
		if (!(response instanceof ServletWebResponse)) {
			throw new IllegalStateException("Cannot be used in a non servlet environment");
		}
		
		return ((ServletWebResponse) response).getContainerResponse();
	}

	private RequestUtils() {
	}

}
