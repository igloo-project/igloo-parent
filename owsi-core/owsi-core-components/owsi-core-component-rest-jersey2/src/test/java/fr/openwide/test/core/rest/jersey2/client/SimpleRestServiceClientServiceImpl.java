package fr.openwide.test.core.rest.jersey2.client;

import java.net.URI;

import javax.ws.rs.client.WebTarget;

import fr.openwide.core.rest.jersey2.client.AbstractRestClientServiceImpl;
import fr.openwide.core.test.jpa.example.business.person.model.Person;

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
