package org.iglooproject.test.rest.jersey2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.iglooproject.test.rest.jersey2.business.person.model.Person;
import org.iglooproject.test.rest.jersey2.business.person.service.IPersonService;
import org.iglooproject.test.rest.jersey2.client.SimpleRestServiceClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TestSimpleRestService extends AbstractRestServiceTestCase {
	
	@Autowired
	private IPersonService personService;
	
	@Test
	void testRest() throws Exception {
		SimpleRestServiceClientServiceImpl client = new SimpleRestServiceClientServiceImpl(getServerResource().getRestUri());
		
		// Entité inexistante
		Person entity;
		entity = client.getEntity(1L);
		assertNull(entity);
		
		// Entité existante
		Person person = new Person("John", "Doe");
		personService.create(person);
		
		entity = client.getEntity(person.getId());
		assertNotNull(entity);
		assertEquals(person.getId(), entity.getId());
		assertEquals(person.getFirstName(), entity.getFirstName());
		assertEquals(person.getLastName(), entity.getLastName());
	}

}
