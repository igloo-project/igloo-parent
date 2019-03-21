package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.flyway.IglooFlywayAutoConfiguration;
import org.igloo.spring.autoconfigure.jpa.IglooJpaAutoConfiguration;
import org.igloo.spring.autoconfigure.search.IglooHibernateSearchAutoConfiguration;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyService;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Joiner;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers IglooPropertyAutoConfiguration properly. 
 * 
 * This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 *  
 */
public class PropertyAutoConfigurationTestCase {

	/**
	 * Check that autoconfiguration from {@link IPropertyService} is triggered with EnableIglooAutoConfiguration
	 */
	@Test
	public void testIglooAutoConfigure() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(TestConfig.class)).run(
				(context) -> { assertThat(context).hasSingleBean(IPropertyService.class); }
			);
	}

	/**
	 * Check that autoconfiguration from {@link IPropertyService} is triggered but {@link IMutablePropertyDao} isn't
	 * when excluding jpa, flyway and hibernate search auto configurations
	 */
	@Test
	public void testIglooNoJpaAutoConfigure() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.withPropertyValues(String.format("spring.autoconfigure.exclude=%s",
					Joiner.on(",").join(IglooJpaAutoConfiguration.class.getName(),
							IglooFlywayAutoConfiguration.class.getName(),
							IglooHibernateSearchAutoConfiguration.class.getName())))
			.run(
				(context) -> {
					assertThat(context).hasSingleBean(IPropertyService.class);
					assertThat(context).doesNotHaveBean(IMutablePropertyDao.class);
				}
			);
	}

	@Configuration
	@EnableIglooAutoConfiguration
	public static class TestConfig {}

}
