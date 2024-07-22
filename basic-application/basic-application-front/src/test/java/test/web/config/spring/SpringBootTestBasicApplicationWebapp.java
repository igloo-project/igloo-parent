package test.web.config.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.test.jpa.junit.EntityManagerExecutionListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@Retention(RUNTIME)
@Target(TYPE)
@SpringBootTest
@ContextConfiguration(
    classes = BasicApplicationWebappTestCommonConfig.class,
    initializers = ExtendedApplicationContextInitializer.class)
@TestPropertySource(properties = "igloo.profile=test")
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      EntityManagerExecutionListener.class
    })
public @interface SpringBootTestBasicApplicationWebapp {}
