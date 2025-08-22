package test.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import igloo.test.listener.IglooTestExecutionListener;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.ApplicationEventsTestExecutionListener;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners({
  // enable dependency injection and events
  ApplicationEventsTestExecutionListener.class,
  DependencyInjectionTestExecutionListener.class,
  // inject our listener
  IglooTestExecutionListener.class
})
@TestPropertySource(
    properties = {
      "testContainer.database.dockerImageName=postgres:alpine",
      "testContainer.database.exposedPorts=5432",
      "testContainer.database.name=test_database",
      "testContainer.database.userName=test_database",
      "testContainer.database.password=test_database",
    })
// use memory for lucene indexes when hsearch is used
@TestPropertySource(
    properties = "spring.jpa.properties.hibernate.search.backend.directory.type=local-heap")
@Retention(RUNTIME)
@Target(TYPE)
public @interface BaseTest {}
