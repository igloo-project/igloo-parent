package org.iglooproject.test.web.context;

import java.io.IOException;
import java.net.URI;

import jakarta.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.iglooproject.slf4j.jul.bridge.SLF4JLoggingListener;

/**
 * How to use :
 * <ul>
 * <li>Annotate your test with {@link MockServletTestExecutionListener}
 * <li>Add a bean of type {@link AbstractMockServlet}
 * </ul>
 */
public abstract class AbstractMockServlet {
	
	private final String schemeAndHost;
	private final int port;
	private final String contextPath;
	
	private HttpServer server;
	
	public AbstractMockServlet(String schemeAndHost, int port, String contextPath) {
		super();
		this.schemeAndHost = schemeAndHost;
		this.port = port;
		this.contextPath = contextPath;
	}
	
	public void prepare() {
		try {
			server = createServer();
			server.start();
		} catch (IOException e) {
			throw new IllegalStateException("Error while initializing tested server", e);
		}
	}

	public void tearDown() {
		if (server != null) {
			server.shutdownNow();
		}
	}
	
	private HttpServer createServer() {
		String contextPath = getContextPath();
		
		WebappContext context = new WebappContext("GrizzlyContext", contextPath);
		
		configureContext(context);
		addServlets(context);
		URI uri = getHostUri();
		
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri);
		context.deploy(server);
		
		return server;
	}

	protected void configureContext(WebappContext context) {
		context.addListener(SLF4JLoggingListener.class);
	}
	
	protected abstract void addServlets(WebappContext context);
	
	public final String getSchemeAndHost() {
		return schemeAndHost;
	}
	
	public final int getPort() {
		return port;
	}
	
	public final String getContextPath() {
		return contextPath;
	}
	
	public final URI getHostUri() {
		return UriBuilder.fromUri(getSchemeAndHost())
				.port(getPort())
				.path(getContextPath())
				.build();
	}
	
	public final URI getContextUri() {
		return UriBuilder.fromUri(getSchemeAndHost())
				.port(getPort())
				.path(getContextPath())
				.build();
	}
}
