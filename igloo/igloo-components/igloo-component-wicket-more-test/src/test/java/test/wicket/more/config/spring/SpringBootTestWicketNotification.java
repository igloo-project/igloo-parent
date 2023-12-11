package test.wicket.more.config.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.jpa.security.autoconfigure.SecurityAutoConfiguration;
import org.iglooproject.test.jpa.junit.EntityManagerExecutionListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@Retention(RUNTIME)
@Target(TYPE)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@SpringBootTest(classes = { TestCommonConfiguration.class, TestNotificationApplicationConfiguration.class })
@ContextConfiguration(initializers = ExtendedApplicationContextInitializer.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	EntityManagerExecutionListener.class,
	DirtiesContextTestExecutionListener.class
})
@DirtiesContext
@TestPropertySource(properties = {
	"igloo.profile=test",
	// let email system perform non-filtered notifications; it is mocked
	"configurationType=deployment"
})
public @interface SpringBootTestWicketNotification {

}