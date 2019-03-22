package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManagerFactory;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.IglooAutoConfigurationImportSelector;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap3AutoConfiguration;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.junit.Test;
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
public class HibernateSearchAutoConfigurationTestCase {

	/**
	 * Check that autoconfiguration from {@link EntityManagerFactory} is triggered with EnableIglooAutoConfiguration
	 */
	@Test
	public void testIglooHibernateSearchAutoConfigure() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.withPropertyValues(String.format("%s=%s",
					IglooAutoConfigurationImportSelector.PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE,
					IglooBootstrap3AutoConfiguration.class.getName()))
			.run(
				(context) -> { assertThat(context).hasSingleBean(IHibernateSearchService.class); }
			);
	}
	
	@Configuration
	@EnableIglooAutoConfiguration
	public static class TestConfig {}

}
