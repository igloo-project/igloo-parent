package org.iglooproject.test.rest.jersey2.client;

import java.net.URI;
import javax.ws.rs.client.WebTarget;
import org.iglooproject.rest.jersey2.client.AbstractRestClientServiceImpl;
import org.iglooproject.test.rest.jersey2.business.person.model.Person;

public class SimpleRestServiceClientServiceImpl extends AbstractRestClientServiceImpl {

  public SimpleRestServiceClientServiceImpl(URI remoteServiceUri) {
    super(remoteServiceUri);
  }

  public SimpleRestServiceClientServiceImpl(String schemeAndHost, int port, String path) {
    super(schemeAndHost, port, path);
  }

  public Person getEntity(Long id) {
    WebTarget target = getTarget().path("test/get/{param}/").resolveTemplate("param", id);

    return target.request().get(Person.class);
  }
}
