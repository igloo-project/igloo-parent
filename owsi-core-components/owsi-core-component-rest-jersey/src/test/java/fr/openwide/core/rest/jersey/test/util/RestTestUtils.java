package fr.openwide.core.rest.jersey.test.util;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.container.grizzly.GrizzlyServerFactory;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

public final class RestTestUtils {
	
	public static SelectorThread getSelectorThread(Class<?> applicationConfigClass,
			String testServerUri, int testServerPort,
			String testContextPath, String testServletPath, String[] springContextFiles, boolean enableJPA)
					throws IllegalArgumentException, IOException {
		final URI baseUri = UriBuilder.fromUri(testServerUri).port(testServerPort).build();
		final ServletAdapter adapter = new ServletAdapter();

		adapter.addInitParameter(ServletContainer.APPLICATION_CONFIG_CLASS, applicationConfigClass.getName());
		adapter.addInitParameter("com.sun.jersey.spi.container.ContainerRequestFilters", LoggingFilter.class.getName());
		adapter.addInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters", LoggingFilter.class.getName());
		
		adapter.addContextParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				StringUtils.arrayToCommaDelimitedString(springContextFiles));

		adapter.addServletListener(ContextLoaderListener.class.getName());
		
		if (enableJPA) {
			adapter.addFilter(new OpenEntityManagerInViewFilter(), "openEntityManagerInViewFilter", null);
		}
		
		adapter.setServletInstance(new SpringServlet());
		adapter.setContextPath(testContextPath);
		adapter.setServletPath(testServletPath);
	
		return GrizzlyServerFactory.create(baseUri, adapter);
	}
	
	public static void closeSelectorThread(SelectorThread selectorThread) {
		selectorThread.stopEndpoint();
	}
	
	private RestTestUtils() {
	}

}