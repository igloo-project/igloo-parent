package org.iglooproject.test.rest.jersey2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.test.jpa.junit.EntityManagerExecutionListener;
import org.iglooproject.test.rest.jersey2.business.person.model.Person;
import org.iglooproject.test.rest.jersey2.business.person.service.IPersonService;
import org.iglooproject.test.rest.jersey2.client.SimpleRestServiceClientServiceImpl;
import org.iglooproject.test.rest.jersey2.config.spring.RestCommonTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/** Run both server and test with the same spring context with a random server port. */
@SpringBootTest(
    classes = RestCommonTestConfiguration.class,
    webEnvironment = WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = ExtendedApplicationContextInitializer.class)
@TestExecutionListeners({
  DependencyInjectionTestExecutionListener.class,
  EntityManagerExecutionListener.class
})
@TestPropertySource(properties = "igloo.profile=test")
class TestSimpleRestService extends AbstractRestServiceTestCase {

  @Autowired private IPersonService personService;

  @LocalServerPort private int port;

  @Test
  void testRest() throws Exception {
    SimpleRestServiceClientServiceImpl client =
        new SimpleRestServiceClientServiceImpl(URI.create("http://localhost:%d".formatted(port)));

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
