package fr.openwide.core.rest.jersey2.client;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public abstract class AbstractRestClientServiceImpl {
	
	private final String schemeAndHost;
	private final int port;
	private final String path;
	
	protected AbstractRestClientServiceImpl(String schemeAndHost, int port, String path) {
		this.schemeAndHost = schemeAndHost;
		this.port = port;
		this.path = path;
	}
	
	protected Client createJerseyClient() {
		return ClientBuilder.newBuilder()
				.register(MultiPartFeature.class)
				.register(JacksonJsonProvider.class)
				.build();
	}
	
	protected String getServiceUrl() {
		URI uri = UriBuilder.fromUri(schemeAndHost)
				.port(port)
				.path(path)
				.build();
		return uri.toString();
	}
	
	protected WebTarget getTarget() {
		return createJerseyClient().target(getServiceUrl());
	}
}