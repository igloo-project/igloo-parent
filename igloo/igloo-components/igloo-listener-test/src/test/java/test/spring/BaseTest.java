package test.spring;

import igloo.test.listener.IglooTestExecutionListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.ApplicationEventsTestExecutionListener;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
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
public @interface BaseTest {}
