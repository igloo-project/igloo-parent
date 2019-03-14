package test.specific;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;

import test.specific.TestSpringBootAloneBehavior.TestConfig;

@ContextConfiguration(classes = { TestConfig.class })
public class TestSpringBootAloneBehavior extends AbstractAutoConfigurationBehaviorTestCase {

	/**
	 * Check that autoconfiguration from {@link MailSenderAutoConfiguration} is triggered normally as we use here
	 * Spring Boot {@link EnableAutoConfiguration}.
	 */
	@Test
	public void testSpringBootAloneAutoConfigure() {
		Assertions
			.assertThatCode(() -> applicationContext.getBean(JavaMailSenderImpl.class))
			.doesNotThrowAnyException();
	}

	@Configuration
	@EnableAutoConfiguration
	public static class TestConfig {
	}
}
