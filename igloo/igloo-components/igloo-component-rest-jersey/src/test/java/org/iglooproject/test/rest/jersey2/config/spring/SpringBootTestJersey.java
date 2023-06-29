package org.iglooproject.test.rest.jersey2.config.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.test.jpa.junit.EntityManagerExecutionListener;
import org.iglooproject.test.web.context.MockServletTestExecutionListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@Retention(RUNTIME)
@Target(TYPE)
@EnableAutoConfiguration
@SpringBootTest(classes = RestClientTestConfiguration.class)
@ContextConfiguration(initializers = ExtendedApplicationContextInitializer.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	MockServletTestExecutionListener.class,
	EntityManagerExecutionListener.class
})
@TestPropertySource(properties = "igloo.profile=test")
public @interface SpringBootTestJersey {

}
