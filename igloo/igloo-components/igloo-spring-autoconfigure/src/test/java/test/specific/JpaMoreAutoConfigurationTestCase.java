package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.flyway.IglooFlywayAutoConfiguration;
import org.igloo.spring.autoconfigure.jpa.IglooJpaAutoConfiguration;
import org.igloo.spring.autoconfigure.jpa.IglooJpaMoreAutoConfiguration;
import org.igloo.spring.autoconfigure.jpa.IglooJpaMoreComponentScanConfig;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.igloo.spring.autoconfigure.search.IglooHibernateSearchAutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.igloo.spring.autoconfigure.task.IglooTaskManagementAutoConfiguration;
import org.igloo.spring.autoconfigure.wicket.IglooWicketAutoConfiguration;
import org.iglooproject.jpa.more.util.init.dao.IImportDataDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers {@link IglooJpaMoreAutoConfiguration}
 * properly. 
 *  
 */
class JpaMoreAutoConfigurationTestCase {

	/**
	 * Check that @{@link ComponentScan} is triggered despite @{@link ConditionalOnBean} (workaround done by splitting
	 * {@link IglooJpaMoreAutoConfiguration} and {@link IglooJpaMoreComponentScanConfig}.
	 * 
	 * cf https://github.com/spring-projects/spring-boot/issues/1625
	 */
	@Test
	void testIglooJpaAutoConfigure() {
		new ApplicationContextRunner()
			.withAllowBeanDefinitionOverriding(true)
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.run(
				(context) -> {
					assertThat(context).hasSingleBean(IglooJpaAutoConfiguration.class);
					assertThat(context).hasSingleBean(IglooJpaMoreAutoConfiguration.class);
					assertThat(context).hasSingleBean(IImportDataDao.class);
				}
			);
	}

	/**
	 * Check that configuration with {@link IglooJpaAutoConfiguration} excluded does not load
	 * {@link IglooJpaMoreAutoConfiguration}.
	 */
	@Test
	void testIglooJpaLessAutoConfigure() {
		new ApplicationContextRunner()
			.withAllowBeanDefinitionOverriding(true)
			.withConfiguration(AutoConfigurations.of(TestJpaLessConfig.class))
			.run(
				(context) -> {
					assertThat(context).doesNotHaveBean(IglooJpaAutoConfiguration.class);
					assertThat(context).doesNotHaveBean(IglooJpaMoreAutoConfiguration.class);
					assertThat(context).doesNotHaveBean(IImportDataDao.class); 
				}
			);
	}

	@Configuration
	@EnableIglooAutoConfiguration(exclude = {IglooJpaSecurityAutoConfiguration.class,
			IglooApplicationConfigAutoConfiguration.class})
	public static class TestConfig {}

	@Configuration
	@EnableIglooAutoConfiguration(exclude = {
			IglooJpaAutoConfiguration.class, IglooHibernateSearchAutoConfiguration.class,
			IglooFlywayAutoConfiguration.class,
			IglooTaskManagementAutoConfiguration.class,
			IglooJpaSecurityAutoConfiguration.class,
			IglooBootstrap4AutoConfiguration.class,
			IglooWicketAutoConfiguration.class,
			IglooPropertyAutoConfiguration.class,
			IglooApplicationConfigAutoConfiguration.class})
	public static class TestJpaLessConfig {
	}

}
