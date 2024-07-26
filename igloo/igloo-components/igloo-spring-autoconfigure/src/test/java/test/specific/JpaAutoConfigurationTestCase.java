package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.base.Joiner;
import javax.persistence.EntityManagerFactory;
import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.IglooAutoConfigurationImportSelector;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap5AutoConfiguration;
import org.igloo.spring.autoconfigure.flyway.IglooFlywayAutoConfiguration;
import org.igloo.spring.autoconfigure.jpa.IglooJpaMoreAutoConfiguration;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.igloo.spring.autoconfigure.search.IglooHibernateSearchAutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.igloo.spring.autoconfigure.task.IglooTaskManagementAutoConfiguration;
import org.igloo.spring.autoconfigure.wicket.IglooWicketAutoConfiguration;
import org.iglooproject.spring.property.service.IPropertyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers
 * IglooJpaAutoConfiguration properly.
 *
 * <p>This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 */
class JpaAutoConfigurationTestCase {

  /**
   * Check that autoconfiguration from {@link EntityManagerFactory} is triggered with
   * EnableIglooAutoConfiguration
   */
  @Test
  void testIglooJpaAutoConfigure() {
    new ApplicationContextRunner()
        .withAllowBeanDefinitionOverriding(true)
        .withConfiguration(AutoConfigurations.of(TestConfig.class))
        .withPropertyValues(
            String.format(
                "%s=%s",
                IglooAutoConfigurationImportSelector.PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE,
                IglooBootstrap4AutoConfiguration.class.getName()))
        .run(context -> assertThat(context).hasSingleBean(EntityManagerFactory.class));
  }

  /**
   * Check that autoconfiguration from {@link EntityManagerFactory} is triggered and that {@link
   * IPropertyService} isn't when excluding property, hibernate search and flyway auto
   * configurations.
   */
  @Test
  void testIglooJpaNoPropertyAutoConfigure() {
    new ApplicationContextRunner()
        .withAllowBeanDefinitionOverriding(true)
        .withConfiguration(AutoConfigurations.of(TestConfig.class))
        .withPropertyValues(
            String.format(
                "%s=%s",
                IglooAutoConfigurationImportSelector.PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE,
                IglooBootstrap4AutoConfiguration.class.getName()))
        .withPropertyValues(
            String.format(
                "%s=%s",
                IglooAutoConfigurationImportSelector.PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE,
                Joiner.on(",")
                    .join(
                        IglooPropertyAutoConfiguration.class.getName(),
                        IglooFlywayAutoConfiguration.class.getName(),
                        IglooHibernateSearchAutoConfiguration.class.getName(),
                        IglooBootstrap5AutoConfiguration.class.getName(),
                        IglooWicketAutoConfiguration.class.getName(),
                        IglooTaskManagementAutoConfiguration.class.getName(),
                        IglooJpaMoreAutoConfiguration.class.getName())))
        .run(
            context -> {
              assertThat(context).hasSingleBean(EntityManagerFactory.class);
              assertThat(context).doesNotHaveBean(IPropertyService.class);
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
