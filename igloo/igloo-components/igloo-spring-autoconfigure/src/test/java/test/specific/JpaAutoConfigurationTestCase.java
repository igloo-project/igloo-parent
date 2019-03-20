package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManagerFactory;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.flyway.IglooFlywayAutoConfiguration;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.igloo.spring.autoconfigure.search.IglooHibernateSearchAutoConfiguration;
import org.iglooproject.spring.property.service.IPropertyService;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Joiner;

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
	public void testIglooJpaAutoConfigure() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(TestConfig.class)).run(
				(context) -> { assertThat(context).hasSingleBean(EntityManagerFactory.class); }
			);
	}
	
	/**
	 * Check that autoconfiguration from {@link EntityManagerFactory} is triggered and that {@link IPropertyService}
	 * isn't when excluding property, hibernate search and flyway auto configurations.
	 */
	@Test
	public void testIglooJpaNoPropertyAutoConfigure() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.withPropertyValues(String.format("spring.autoconfigure.exclude=%s",
					Joiner.on(",").join(IglooPropertyAutoConfiguration.class.getName(),
							IglooFlywayAutoConfiguration.class.getName(),
							IglooHibernateSearchAutoConfiguration.class.getName())))
			.run(
				(context) -> {
					assertThat(context).hasSingleBean(EntityManagerFactory.class);
					assertThat(context).doesNotHaveBean(IPropertyService.class);
				}
			);
	}

	@Configuration
	@EnableIglooAutoConfiguration
	public static class TestConfig {}

}
