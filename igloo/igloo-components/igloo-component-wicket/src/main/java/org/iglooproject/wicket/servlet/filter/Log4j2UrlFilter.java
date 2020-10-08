package org.iglooproject.wicket.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.MDC;

public class Log4j2UrlFilter implements Filter {

	private static final String LOG4J_URL_FILTER_MAP_KEY = "ow-url";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			StringBuilder builder = new StringBuilder();
			builder.append(httpRequest.getRequestURI());
			if (httpRequest.getQueryString() != null) {
				builder.append("?");
				builder.append(httpRequest.getQueryString());
			}
			MDC.put(LOG4J_URL_FILTER_MAP_KEY, builder.toString());
		}
		try {
			chain.doFilter(request, response);
		} finally {
			MDC.remove(LOG4J_URL_FILTER_MAP_KEY);
		}
	}

	@Override
	public void destroy() {
	}

}
