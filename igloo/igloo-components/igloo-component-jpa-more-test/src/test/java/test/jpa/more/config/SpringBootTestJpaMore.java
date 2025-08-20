package test.jpa.more.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.test.jpa.junit.EntityManagerExecutionListener;
import org.iglooproject.test.jpa.junit.JpaOnlyTestConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableAutoConfiguration
@ContextConfiguration(initializers = ExtendedApplicationContextInitializer.class)
@SpringBootTest(classes = {TestConfig.class, JpaOnlyTestConfiguration.class})
@TestExecutionListeners({
  DependencyInjectionTestExecutionListener.class,
  EntityManagerExecutionListener.class
})
@TestPropertySource(properties = "igloo.profile=test")
@TestPropertySource(locations = "/jpa-more-test.properties")
public @interface SpringBootTestJpaMore {}
