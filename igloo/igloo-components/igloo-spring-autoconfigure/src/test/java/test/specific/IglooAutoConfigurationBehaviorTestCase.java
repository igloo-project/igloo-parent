package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers only Igloo auto-configuration,
 * without Spring Boot auto-configuration. Tests are :
 * 
 * <ul>
 *  <li>Both configuration behavior</li>
 * 	<li>Spring Boot alone behavior, as a reference behavior (test for Java MailSender bean)</li>
 *  <li>Igloo auto-configuration behavior</li>
 * </ul>
 * 
 * This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 * 
 *  
 * This test relies on the fact that javax.mail and spring-mail is available on classpath.
 * 
 */
public class IglooAutoConfigurationBehaviorTestCase {

	/**
	 * Check that autoconfiguration from {@link MailSenderAutoConfiguration} is triggered normally as we use here both
	 * Spring Boot {@link EnableAutoConfiguration} and IglooAutoConfigure.
	 */
	@Test
	public void testBothSpringBootIglooAutoConfigure() {
		new ApplicationContextRunner()
			.withAllowBeanDefinitionOverriding(true)
			.withPropertyValues("spring.mail.host=localhost")
			.withConfiguration(AutoConfigurations.of(TestBothSpringBootIglooConfig.class))
			.run(
				(context) -> { assertThat(context).hasSingleBean(JavaMailSenderImpl.class); }
			);
	}

	/**
	 * Check that autoconfiguration from {@link MailSenderAutoConfiguration} is triggered normally as we use here
	 * Spring Boot {@link EnableAutoConfiguration}.
	 */
	@Test
	public void testSpringBootAutoConfigure() {
		new ApplicationContextRunner()
			.withAllowBeanDefinitionOverriding(true)
			.withPropertyValues("spring.mail.host=localhost")
			.withConfiguration(AutoConfigurations.of(TestSpringBootConfig.class))
			.run(
				(context) -> { assertThat(context).hasSingleBean(JavaMailSenderImpl.class); }
			);
	}

	/**
	 * Check that autoconfiguration from {@link MailSenderAutoConfiguration} is not triggered despite spring.mail.host
	 * setting as we want to inhibit all Spring Boot autoconfiguration
	 */
	@Test
	public void testIglooAutoConfigure() {
		new ApplicationContextRunner()
			.withAllowBeanDefinitionOverriding(true)
			.withPropertyValues("spring.mail.host=localhost")
			.withConfiguration(AutoConfigurations.of(TestIglooConfig.class))
			.run(
				(context) -> { assertThat(context).doesNotHaveBean(JavaMailSenderImpl.class); }
			);
	}

	@Configuration
	@EnableAutoConfiguration
	@EnableIglooAutoConfiguration(exclude = {IglooJpaSecurityAutoConfiguration.class,
			IglooApplicationConfigAutoConfiguration.class})
	public static class TestBothSpringBootIglooConfig {
	}

	@Configuration
	@EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
	public static class TestSpringBootConfig {
	}

	@Configuration
	@EnableIglooAutoConfiguration(exclude = {IglooJpaSecurityAutoConfiguration.class,
			IglooApplicationConfigAutoConfiguration.class})
	public static class TestIglooConfig {
	}
}
