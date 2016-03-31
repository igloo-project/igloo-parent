package fr.openwide.test.core.rest.jersey2.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.openwide.core.rest.jersey2.service.AbstractRestServiceImpl;
import fr.openwide.test.core.rest.jersey2.business.person.model.Person;
import fr.openwide.test.core.rest.jersey2.business.person.service.IPersonService;

@Component
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class SimpleRestServiceImpl extends AbstractRestServiceImpl {
	
	@Autowired
	private IPersonService personService;
	
	@GET
	@Path("get/{id}/")
	public Person get(@PathParam("id") Long id) {
		return personService.getById(id);
	}

}