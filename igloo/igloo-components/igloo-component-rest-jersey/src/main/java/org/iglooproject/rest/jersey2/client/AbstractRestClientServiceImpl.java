package org.iglooproject.rest.jersey2.client;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public abstract class AbstractRestClientServiceImpl {

  private final URI targetUri;

  protected AbstractRestClientServiceImpl(String schemeAndHost, int port, String path) {
    this(UriBuilder.fromUri(schemeAndHost).port(port).path(path).build());
  }

  protected AbstractRestClientServiceImpl(URI remoteServiceUri) {
    this.targetUri = remoteServiceUri;
  }

  protected Client createJerseyClient() {
    return ClientBuilder.newBuilder()
        .register(MultiPartFeature.class)
        .register(JacksonJsonProvider.class)
        .build();
  }

  protected URI getTargetUri() {
    return targetUri;
  }

  protected WebTarget getTarget() {
    return createJerseyClient().target(targetUri);
  }
}
