package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManagerFactory;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers IglooJpaAutoConfiguration properly. 
 * 
 * This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 *  
 */
public class JpaAutoConfigurationTestCase {

	/**
	 * Check that autoconfiguration from {@link EntityManagerFactory} is triggered with EnableIglooAutoConfiguration
	 */
	@Test
	public void testIglooAutoConfigure() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(TestIglooConfig.class)).run(
				(context) -> { assertThat(context).hasSingleBean(EntityManagerFactory.class); }
			);
	}

	@Configuration
	@EnableIglooAutoConfiguration
	public static class TestIglooConfig {}

}
