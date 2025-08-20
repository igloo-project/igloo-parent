package org.iglooproject.test.jpa.security.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.test.jpa.junit.EntityManagerExecutionListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableAutoConfiguration
@SpringBootTest(classes = TestJpaSecurityConfiguration.class)
@ContextConfiguration(initializers = ExtendedApplicationContextInitializer.class)
@TestExecutionListeners({
  DependencyInjectionTestExecutionListener.class,
  EntityManagerExecutionListener.class
})
@TestPropertySource(
    properties = {
      "igloo.profile=test",
      "spring.jpa.properties.hibernate.search.enabled=false",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.igloo.old-style-transaction-advisor=true",
      "igloo.system.roles=ROLE_SYSTEM ROLE_ADMIN ROLE_AUTHENTICATED ROLE_ANONYMOUS"
    })
public @interface SpringBootTestJpaSecurity {}
