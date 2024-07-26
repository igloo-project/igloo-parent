package org.iglooproject.rest.jersey2.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
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
