package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.iglooproject.jpa.more.config.util.FlywayConfiguration;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.RunAsManager;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers IglooFlywayAutoConfiguration properly. 
 * 
 * This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 *  
 */
public class JpaSecurityAutoConfigurationTestCase {

	/**
	 * Check that autoconfiguration from {@link FlywayConfiguration} is triggered with EnableIglooAutoConfiguration
	 */
	@Test
	public void testIglooJpaSecurityAutoConfigure() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.withPropertyValues("security.runAsKey=aaa")
			.run(
				(context) -> { assertThat(context).hasSingleBean(RunAsManager.class); }
			);
	}
	
	@Configuration
	@EnableIglooAutoConfiguration(exclude = {IglooApplicationConfigAutoConfiguration.class})
	public static class TestConfig {}

}
