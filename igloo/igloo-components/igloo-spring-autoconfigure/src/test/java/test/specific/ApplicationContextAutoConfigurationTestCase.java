package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.iglooproject.config.bootstrap.spring.ExtendedTestApplicationContextInitializer;
import org.iglooproject.spring.util.ConfigurationLogger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers IglooFlywayAutoConfiguration properly. 
 * 
 * This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 *  
 */
class ApplicationContextAutoConfigurationTestCase {

	/**
	 * Check that autoconfiguration from {@link IglooPropertyAutoConfiguration} is triggered with EnableIglooAutoConfiguration
	 */
	@Test
	void testIglooApplicationConfigAutoConfigure() {
		new ApplicationContextRunner()
			.withAllowBeanDefinitionOverriding(true)
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.withInitializer(new ExtendedTestApplicationContextInitializer())
			.withPropertyValues("igloo.profile=test")
			.run(
				context -> assertThat(context).hasSingleBean(ConfigurationLogger.class)
			);
	}
	
	@Configuration
	@EnableIglooAutoConfiguration(
		exclude = {
			IglooBootstrap4AutoConfiguration.class,
			IglooJpaSecurityAutoConfiguration.class
		}
	)
	public static class TestConfig {}

}
