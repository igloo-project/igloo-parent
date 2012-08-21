package fr.openwide.core.rest.jersey.client;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.impl.provider.entity.MimeMultipartProvider;

public abstract class AbstractRestClientServiceImpl {
	
	private String urlSchemeHost = "http://localhost";

	private int urlPort = 8080;
	
	private String contextPath;
	
	private String restServletPath;
	
	protected AbstractRestClientServiceImpl(String urlSchemeHost, int urlPort, String contextPath,
			String restServletPath) {
		this.urlSchemeHost = urlSchemeHost;
		this.urlPort = urlPort;
		this.contextPath = contextPath;
		this.restServletPath = restServletPath;
	}
	
	protected Client createJerseyClient() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(MimeMultipartProvider.class);
		clientConfig.getClasses().add(JacksonJsonProvider.class);
		
		return Client.create(clientConfig);
	}
	
	protected String getServiceUrl(String path, Object... parameters) {
		URI uri = UriBuilder.fromUri(urlSchemeHost)
				.port(urlPort)
				.path(contextPath)
				.path(restServletPath)
				.path(String.format(path, parameters)).build();
		return uri.toString();
	}
}