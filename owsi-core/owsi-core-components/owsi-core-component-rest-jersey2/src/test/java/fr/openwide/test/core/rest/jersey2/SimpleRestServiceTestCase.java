package fr.openwide.test.core.rest.jersey2;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.rest.jersey2.test.RestServerTestResource;
import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.config.spring.JpaTestConfig;
import fr.openwide.core.test.jpa.example.business.person.model.Person;
import fr.openwide.core.test.jpa.example.business.person.service.PersonService;
import fr.openwide.test.core.rest.jersey2.client.SimpleRestServiceClientServiceImpl;

public class SimpleRestServiceTestCase extends AbstractJpaCoreTestCase {
	
	@Autowired
	private PersonService personService;
	
	@Rule
	public final RestServerTestResource serverResource = new RestServerTestResource("http://localhost/", 9997, "/sqm-remote-api", "/rest", JpaTestConfig.class) {
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
