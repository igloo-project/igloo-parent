package fr.openwide.core.wicket.more.util;

import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;

public final class RequestUtils {
	
	private static final String QUERY_STRING_SEPARATOR = "?";
	
	public static String getCurrentRequestUrl() {
		Request request = RequestCycle.get().getRequest();
		if (!(request instanceof ServletWebRequest)) {
			return null;
		}
		
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		StringBuffer currentUrl = servletWebRequest.getContainerRequest().getRequestURL();
		String queryString = servletWebRequest.getContainerRequest().getQueryString();
		if (queryString != null) {
			currentUrl.append(QUERY_STRING_SEPARATOR).append(queryString);
		}
		
		return currentUrl.toString();
	}

	private RequestUtils() {
	}

}
