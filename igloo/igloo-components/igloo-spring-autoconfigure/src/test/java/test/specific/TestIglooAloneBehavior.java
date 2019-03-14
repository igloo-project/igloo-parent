package test.specific;

import org.assertj.core.api.Assertions;
import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;

import test.specific.TestIglooAloneBehavior.TestConfig;

@ContextConfiguration(classes = { TestConfig.class })
public class TestIglooAloneBehavior extends AbstractAutoConfigurationBehaviorTestCase {

	/**
	 * Check that autoconfiguration from {@link MailSenderAutoConfiguration} is not triggered despite spring.mail.host
	 * setting as we want to inhibit all Spring Boot autoconfiguration
	 */
	@Test
	public void testIglooAloneAutoConfigure() {
		Assertions
			.assertThatThrownBy(() -> applicationContext.getBean(JavaMailSenderImpl.class))
			.isInstanceOf(NoSuchBeanDefinitionException.class);
	}

	@Configuration
	@EnableIglooAutoConfiguration
	public static class TestConfig {
	}
}
