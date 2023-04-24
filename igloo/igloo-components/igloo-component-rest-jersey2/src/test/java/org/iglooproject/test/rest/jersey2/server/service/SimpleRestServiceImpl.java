package org.iglooproject.test.rest.jersey2.server.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.iglooproject.rest.jersey2.service.AbstractRestServiceImpl;
import org.iglooproject.test.rest.jersey2.business.person.model.Person;
import org.iglooproject.test.rest.jersey2.business.person.service.IPersonService;

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