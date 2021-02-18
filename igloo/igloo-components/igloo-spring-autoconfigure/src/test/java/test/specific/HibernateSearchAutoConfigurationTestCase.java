package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers IglooJpaAutoConfiguration properly. 
 * 
 * This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 *  
 */
class HibernateSearchAutoConfigurationTestCase {

	/**
	 * Check that autoconfiguration from {@link IHibernateSearchService} is triggered with EnableIglooAutoConfiguration
	 */
	@Test
	void testIglooHibernateSearchAutoConfigure() {
		new ApplicationContextRunner()
			.withAllowBeanDefinitionOverriding(true)
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.run(
				context -> assertThat(context).hasSingleBean(IHibernateSearchService.class)
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
