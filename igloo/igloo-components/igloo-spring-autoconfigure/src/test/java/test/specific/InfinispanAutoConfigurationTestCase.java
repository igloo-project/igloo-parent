package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.IglooAutoConfigurationImportSelector;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap3AutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.iglooproject.infinispan.service.IInfinispanClusterService;
import org.iglooproject.jpa.more.config.util.FlywayConfiguration;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Joiner;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers IglooFlywayAutoConfiguration properly. 
 * 
 * This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 *  
 */
public class InfinispanAutoConfigurationTestCase {

	/**
	 * Check that autoconfiguration from {@link FlywayConfiguration} is triggered with EnableIglooAutoConfiguration
	 */
	@Test
	public void testIglooInfinispanAutoConfigure() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.withPropertyValues(String.format("%s=%s",
					IglooAutoConfigurationImportSelector.PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE,
					Joiner.on(",").join(IglooBootstrap3AutoConfiguration.class.getName(),
							IglooJpaSecurityAutoConfiguration.class.getName())))
			.run(
				(context) -> { assertThat(context).hasSingleBean(IInfinispanClusterService.class); }
			);
	}
	
	@Configuration
	@EnableIglooAutoConfiguration
	public static class TestConfig {}

}
