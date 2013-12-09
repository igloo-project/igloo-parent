package fr.openwide.core.rest.jersey2.test;

import java.io.IOException;
import java.net.URI;

import javax.servlet.Servlet;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.After;
import org.junit.Before;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import fr.openwide.core.commons.util.logging.SLF4JLoggingListener;
import fr.openwide.core.spring.config.ExtendedApplicationContextInitializer;
import fr.openwide.core.test.AbstractJpaCoreTestCase;

public abstract class AbstractRestServiceTestCase extends AbstractJpaCoreTestCase {
	
	private final String schemeAndHost;
	private final int port;
	private final String contextPath;
	private final String servletPath;
	
	private HttpServer server;
	
	public AbstractRestServiceTestCase(String schemeAndHost, int port, String contextPath, String servletPath) {
		super();
		this.schemeAndHost = schemeAndHost;
		this.port = port;
		this.contextPath = contextPath;
		this.servletPath = servletPath;
	}

	@Before
	public final void startServer() throws Exception {
		ResourceConfig resource = createApplication();
		try {
			server = createServer(resource);
			server.start();
		} catch (IOException e) {
			throw new IllegalStateException("Error while initializing tested server", e);
		}
	}

	@After
	public final void stopServer() {
		if (server != null) {
			server.stop();
		}
	}
	
	private HttpServer createServer(ResourceConfig resourceConfig) {
		String contextPath = getContextPath();
		
		WebappContext context = new WebappContext("GrizzlyContext", contextPath);
		
		configureContext(context);
		
		addRestApplicationServlet(context, resourceConfig);
		
		URI uri = getBaseUri();
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri);
		context.deploy(server);
		
		return server;
	}

	protected void configureContext(WebappContext context) {
		context.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM, AnnotationConfigWebApplicationContext.class.getName());
		context.setInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM, ExtendedApplicationContextInitializer.class.getName());
		Class<?> javaConfigClass = getJavaConfigClass();
		if (javaConfigClass != null) {
			context.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, javaConfigClass.getName());
		}
		
		context.addListener(SLF4JLoggingListener.class);
		context.addListener(ContextLoaderListener.class);
		context.addFilter("openEntityManagerInViewFilter", new OpenEntityManagerInViewFilter())
				.addMappingForUrlPatterns(null, "/*");
	}
	
	protected void addRestApplicationServlet(WebappContext context, ResourceConfig resourceConfig) {
		Servlet restFilter = new ServletContainer(resourceConfig);
		ServletRegistration restFilterRegistration = context.addServlet("restFilter", restFilter);
		restFilterRegistration.addMapping(getServletPath() + "/*");
	}
	
	protected final String getSchemeAndHost() {
		return schemeAndHost;
	}
	
	protected final int getPort() {
		return port;
	}
	
	protected final String getContextPath() {
		return contextPath;
	}
	
	protected final String getServletPath() {
		return servletPath;
	}
	
	protected final URI getBaseUri() {
		return UriBuilder.fromUri(getSchemeAndHost())
				.port(getPort())
				.path(getContextPath())
				.path(getServletPath())
				.build();
	}

	protected abstract ResourceConfig createApplication();
	
	protected abstract Class<?> getJavaConfigClass();

}
