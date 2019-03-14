package test.specific;

import org.assertj.core.api.Assertions;
import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;

import test.specific.TestBothSpringBootIglooBehavior.TestConfig;

@ContextConfiguration(classes = { TestConfig.class })
public class TestBothSpringBootIglooBehavior extends AbstractAutoConfigurationBehaviorTestCase {

	/**
	 * Check that autoconfiguration from {@link MailSenderAutoConfiguration} is triggered normally as we use here
	 * Spring Boot {@link EnableAutoConfiguration}.
	 */
	@Test
	public void testBothSpringBootIglooAutoConfigure() {
		Assertions
			.assertThatCode(() -> applicationContext.getBean(JavaMailSenderImpl.class))
			.doesNotThrowAnyException();
	}

	@Configuration
	@EnableAutoConfiguration
	@EnableIglooAutoConfiguration
	public static class TestConfig {
	}
}
