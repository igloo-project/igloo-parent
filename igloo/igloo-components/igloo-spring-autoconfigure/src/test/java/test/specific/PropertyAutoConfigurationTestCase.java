package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.base.Joiner;
import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.IglooAutoConfigurationImportSelector;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap5AutoConfiguration;
import org.igloo.spring.autoconfigure.flyway.IglooFlywayAutoConfiguration;
import org.igloo.spring.autoconfigure.jpa.IglooJpaAutoConfiguration;
import org.igloo.spring.autoconfigure.jpa.IglooJpaMoreAutoConfiguration;
import org.igloo.spring.autoconfigure.search.IglooHibernateSearchAutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.igloo.spring.autoconfigure.task.IglooTaskManagementAutoConfiguration;
import org.igloo.spring.autoconfigure.wicket.IglooWicketAutoConfiguration;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers
 * IglooPropertyAutoConfiguration properly.
 *
 * <p>This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 */
class PropertyAutoConfigurationTestCase {

  /**
   * Check that autoconfiguration from {@link IPropertyService} is triggered with
   * EnableIglooAutoConfiguration
   */
  @Test
  void testIglooPropertyAutoConfigure() {
    new ApplicationContextRunner()
        .withAllowBeanDefinitionOverriding(true)
        .withConfiguration(AutoConfigurations.of(TestConfig.class))
        .run(
            (context) -> {
              assertThat(context).hasSingleBean(IPropertyService.class);
            });
  }

  /**
   * Check that autoconfiguration from {@link IPropertyService} is triggered but {@link
   * IMutablePropertyDao} isn't when excluding jpa, flyway and hibernate search auto configurations
   */
  @Test
  void testIglooPropertyNoJpaAutoConfigure() {
    new ApplicationContextRunner()
        .withAllowBeanDefinitionOverriding(true)
        .withConfiguration(AutoConfigurations.of(TestConfig.class))
        .withPropertyValues(
            String.format(
                "%s=%s",
                IglooAutoConfigurationImportSelector.PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE,
                Joiner.on(",")
                    .join(
                        IglooJpaAutoConfiguration.class.getName(),
                        IglooJpaMoreAutoConfiguration.class.getName(),
                        IglooFlywayAutoConfiguration.class.getName(),
                        IglooHibernateSearchAutoConfiguration.class.getName(),
                        IglooBootstrap5AutoConfiguration.class.getName(),
                        IglooWicketAutoConfiguration.class.getName(),
                        IglooJpaSecurityAutoConfiguration.class.getName(),
                        IglooTaskManagementAutoConfiguration.class.getName())))
        .withPropertyValues("propertyNamesForInfoLogLevel=version")
        .run(
            context -> {
              assertThat(context).hasSingleBean(IPropertyService.class);
              assertThat(context).doesNotHaveBean(IMutablePropertyDao.class);
            });
  }

  @Configuration
  @EnableIglooAutoConfiguration(
      exclude = {
        IglooBootstrap4AutoConfiguration.class,
        IglooJpaSecurityAutoConfiguration.class,
        IglooApplicationConfigAutoConfiguration.class
      })
  public static class TestConfig {}
}
