package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers IglooFlywayAutoConfiguration properly. 
 * 
 * This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 *  
 */
class FlywayAutoConfigurationTestCase {

	/**
	 * Check that autoconfiguration from {@link FlywayAutoConfiguration} is triggered with EnableIglooAutoConfiguration
	 */
	@Test
	void testIglooFlywayAutoConfigure() {
		new ApplicationContextRunner()
			.withAllowBeanDefinitionOverriding(true)
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.run(
				context -> assertThat(context).hasSingleBean(Flyway.class)
			);
	}
	
	@Configuration
	@EnableIglooAutoConfiguration(
		exclude = {
			IglooBootstrap4AutoConfiguration.class,
			IglooJpaSecurityAutoConfiguration.class,
			IglooApplicationConfigAutoConfiguration.class
		}
	)
	public static class TestConfig {}

}
