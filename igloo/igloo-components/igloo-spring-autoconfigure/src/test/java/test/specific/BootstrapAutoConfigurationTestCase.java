package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.IglooAutoConfigurationImportSelector;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap5AutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers
 * IglooBootstrapAutoConfiguration properly.
 *
 * <p>This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 */
class BootstrapAutoConfigurationTestCase {

  /**
   * Check that autoconfiguration from bootstrap 4 {@link
   * igloo.bootstrap4.application.WicketBootstrap4Module} is triggered with
   * EnableIglooAutoConfiguration when excluding bootstrap 5
   */
  @Test
  void testIglooBootstrap4AutoConfigure() {
    new ApplicationContextRunner()
        .withAllowBeanDefinitionOverriding(true)
        .withConfiguration(AutoConfigurations.of(TestConfig.class))
        .withPropertyValues(
            String.format(
                "%s=%s",
                IglooAutoConfigurationImportSelector.PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE,
                IglooBootstrap5AutoConfiguration.class.getName()))
        .run(
            (context) -> {
              assertThat(context)
                  .hasSingleBean(igloo.bootstrap4.application.WicketBootstrap4Module.class);
              assertThat(context)
                  .doesNotHaveBean(igloo.bootstrap5.application.WicketBootstrap5Module.class);
            });
  }

  /**
   * Check that autoconfiguration from bootstrap 5 {@link
   * igloo.bootstrap5.application.WicketBootstrap5Module} is triggered with
   * EnableIglooAutoConfiguration when excluding bootstrap 4
   */
  @Test
  public void testIglooBootstrap5AutoConfigure() {
    new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(TestConfig.class))
        .withPropertyValues(
            String.format(
                "%s=%s",
                IglooAutoConfigurationImportSelector.PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE,
                IglooBootstrap4AutoConfiguration.class.getName()))
        .run(
            (context) -> {
              assertThat(context)
                  .hasSingleBean(igloo.bootstrap5.application.WicketBootstrap5Module.class);
              assertThat(context)
                  .doesNotHaveBean(igloo.bootstrap4.application.WicketBootstrap4Module.class);
            });
  }

  @Configuration
  @EnableIglooAutoConfiguration(
      exclude = {
        IglooJpaSecurityAutoConfiguration.class,
        IglooApplicationConfigAutoConfiguration.class
      })
  public static class TestConfig {}
}
