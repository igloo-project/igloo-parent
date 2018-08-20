package org.iglooproject.test.rest.jersey2;

import org.iglooproject.test.rest.jersey2.business.person.model.Person;
import org.iglooproject.test.rest.jersey2.business.person.service.IPersonService;
import org.iglooproject.test.rest.jersey2.client.SimpleRestServiceClientServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSimpleRestService extends AbstractRestServiceTestCase {
	
	@Autowired
	private IPersonService personService;
	
	@Test
	public void testRest() throws Exception {
		SimpleRestServiceClientServiceImpl client = new SimpleRestServiceClientServiceImpl(getServerResource().getRestUri());
		
		// Entité inexistante
		Person entity;
		entity = client.getEntity(1L);
		Assert.assertNull(entity);
		
		// Entité existante
		Person person = new Person("John", "Doe");
		personService.create(person);
		
		entity = client.getEntity(person.getId());
		Assert.assertNotNull(entity);
		Assert.assertEquals(person.getId(), entity.getId());
		Assert.assertEquals(person.getFirstName(), entity.getFirstName());
		Assert.assertEquals(person.getLastName(), entity.getLastName());
	}

}
