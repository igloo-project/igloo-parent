package fr.openwide.test.core.rest.jersey2;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.rest.jersey2.test.AbstractRestServiceTestCase;
import fr.openwide.core.test.config.spring.JpaTestConfig;
import fr.openwide.core.test.jpa.example.business.person.model.Person;
import fr.openwide.core.test.jpa.example.business.person.service.PersonService;
import fr.openwide.test.core.rest.jersey2.client.SimpleRestServiceClientServiceImpl;

public class SimpleRestServiceTestCase extends AbstractRestServiceTestCase {
	
	@Autowired
	private PersonService personService;
	
	public SimpleRestServiceTestCase() {
		super("http://localhost/", 9997, "/sqm-remote-api", "/rest");
	}

	@Override
	protected ResourceConfig createApplication() {
		return new SimpleRestApplication();
	}
	
	@Override
	protected Class<?> getJavaConfigClass() {
		return JpaTestConfig.class;
	}
	
	@Test
	public void testRest() throws Exception {
		SimpleRestServiceClientServiceImpl client = new SimpleRestServiceClientServiceImpl(getSchemeAndHost(), getPort(), getContextPath() + "/" + getServletPath());
		
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
