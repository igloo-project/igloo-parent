package org.iglooproject.test.rest.jersey2;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.iglooproject.rest.jersey2.test.RestServerTestResource;
import org.iglooproject.test.rest.jersey2.business.person.model.Person;
import org.iglooproject.test.rest.jersey2.business.person.service.IPersonService;
import org.iglooproject.test.rest.jersey2.client.SimpleRestServiceClientServiceImpl;
import org.iglooproject.test.rest.jersey2.server.config.spring.RestServerTestCoreCommonConfig;

public class TestSimpleRestService extends AbstractRestServiceTestCase {
	
	@Autowired
	private IPersonService personService;
	
	@Rule
	public final RestServerTestResource serverResource = new RestServerTestResource(
			"http://localhost/", 9997, "/sqm-remote-api", "/rest", RestServerTestCoreCommonConfig.class) {
		@Override
		protected ResourceConfig createApplication() {
			return new SimpleRestApplication();
		}
	};
	
	@Test
	public void testRest() throws Exception {
		SimpleRestServiceClientServiceImpl client = new SimpleRestServiceClientServiceImpl(serverResource.getBaseUri());
		
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
