package fr.openwide.test.core.rest.jersey2.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.openwide.core.rest.jersey2.service.AbstractRestServiceImpl;
import fr.openwide.core.test.jpa.example.business.person.model.Person;
import fr.openwide.core.test.jpa.example.business.person.service.PersonService;

@Component
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class SimpleRestServiceImpl extends AbstractRestServiceImpl {
	
	@Autowired
	private PersonService personService;

	@GET
	@Path("get/{id}/")
	public Person get(@PathParam("id") Long id) {
		return personService.getById(id);
	}

}