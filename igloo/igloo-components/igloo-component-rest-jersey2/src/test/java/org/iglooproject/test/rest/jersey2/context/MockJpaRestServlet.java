package org.iglooproject.test.rest.jersey2.context;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.iglooproject.test.rest.jersey2.SimpleRestApplication;
import org.iglooproject.test.rest.jersey2.server.config.spring.RestServerTestCoreCommonConfig;
import org.iglooproject.test.web.context.MockSpringServlet;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

public class MockJpaRestServlet extends MockSpringServlet {

	private final String servletPath;

	public MockJpaRestServlet(String schemeAndHost, int port, String contextPath, String servletPath) {
		super(schemeAndHost, port, contextPath, RestServerTestCoreCommonConfig.class);
		this.servletPath = servletPath;
	}

	protected ResourceConfig createApplication() {
		return new SimpleRestApplication();
	}

	@Override
	protected void configureContext(WebappContext context) {
		super.configureContext(context);
		
		context.addFilter("openEntityManagerInViewFilter", new OpenEntityManagerInViewFilter())
				.addMappingForUrlPatterns(null, "/*");
	}
	
	@Override
	protected void addServlets(WebappContext context) {
		ServletContainer restFilter = new ServletContainer(createApplication());
		
		ServletRegistration restFilterRegistration = context.addServlet("restFilter", restFilter);
		// wilcard matching is mandatory
		String url = UriBuilder.fromUri("/").path(getServletPath()).path("*").build().toString();
		restFilterRegistration.addMapping(url);
	}
	
	public final String getServletPath() {
		return servletPath;
	}
	
	public final URI getRestUri() {
		return UriBuilder.fromUri(getSchemeAndHost())
				.port(getPort())
				.path(getContextPath())
				.path(getServletPath())
				.build();
	}

}
