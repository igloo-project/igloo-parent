package fr.openwide.core.rest.jersey.test.util;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.container.grizzly.GrizzlyServerFactory;
import com.sun.jersey.core.impl.provider.entity.MimeMultipartProvider;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

import fr.openwide.core.spring.config.ExtendedApplicationContextInitializer;

public final class RestTestUtils {
	
	public static SelectorThread getSelectorThread(Class<?> applicationConfigClass,
			String testServerUri, int testServerPort,
			String testContextPath, String testServletPath, String[] springContextFiles, boolean enableJPA)
					throws IllegalArgumentException, IOException {
		final URI baseUri = UriBuilder.fromUri(testServerUri).port(testServerPort).build();
		
		final ServletAdapter adapter = initRestTestServletAdapter(applicationConfigClass, testServerPort,
				testContextPath, testServletPath, enableJPA);
		
		adapter.addContextParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				StringUtils.arrayToCommaDelimitedString(springContextFiles));
		
		return GrizzlyServerFactory.create(baseUri, adapter);
	}
	
	public static SelectorThread getSelectorThread(Class<?> applicationConfigClass, String testServerUri, int testServerPort,
			String testContextPath, String testServletPath, Class<?> javaConfigClass, boolean enableJPA)
			throws IllegalArgumentException, IOException {
		final URI baseUri = UriBuilder.fromUri(testServerUri).port(testServerPort).build();
		
		final ServletAdapter adapter = initRestTestServletAdapter(applicationConfigClass, testServerPort,
				testContextPath, testServletPath, enableJPA);
		
		adapter.addContextParameter(ContextLoader.CONTEXT_CLASS_PARAM, AnnotationConfigWebApplicationContext.class.getName());
		adapter.addContextParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM, ExtendedApplicationContextInitializer.class.getName());
		adapter.addContextParameter(ContextLoader.CONFIG_LOCATION_PARAM, javaConfigClass.getName());
		
		return GrizzlyServerFactory.create(baseUri, adapter);
	}
	
	public static ServletAdapter initRestTestServletAdapter(Class<?> applicationConfigClass, int testServerPort,
			String testContextPath, String testServletPath, boolean enableJPA) {
		final ServletAdapter adapter = new ServletAdapter();
		
		adapter.addInitParameter(ServletContainer.APPLICATION_CONFIG_CLASS, applicationConfigClass.getName());
		adapter.addInitParameter("com.sun.jersey.spi.container.ContainerRequestFilters", LoggingFilter.class.getName());
		adapter.addInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters", LoggingFilter.class.getName());
		
		adapter.addServletListener(ContextLoaderListener.class.getName());
		
		if (enableJPA) {
			adapter.addFilter(new OpenEntityManagerInViewFilter(), "openEntityManagerInViewFilter", null);
		}
		
		adapter.setServletInstance(new SpringServlet());
		adapter.setContextPath(testContextPath);
		adapter.setServletPath(testServletPath);
		
		return adapter;
	}
	
	public static void closeSelectorThread(SelectorThread selectorThread) {
		selectorThread.stopEndpoint();
	}
	
	public static Client createJerseyClient() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(MimeMultipartProvider.class);
		clientConfig.getClasses().add(JacksonJsonProvider.class);
		
		return Client.create(clientConfig);
	}
	
	private RestTestUtils() {
	}

}