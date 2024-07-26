package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.RunAsManager;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers
 * IglooFlywayAutoConfiguration properly.
 *
 * <p>This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 */
class JpaSecurityAutoConfigurationTestCase {

  /**
   * Check that autoconfiguration from {@link IglooJpaSecurityAutoConfiguration} is triggered with
   * EnableIglooAutoConfiguration
   */
  @Test
  void testIglooJpaSecurityAutoConfigure() {
    new ApplicationContextRunner()
        .withAllowBeanDefinitionOverriding(true)
        .withConfiguration(AutoConfigurations.of(TestConfig.class))
        .withPropertyValues("security.runAsKey=aaa")
        .run(context -> assertThat(context).hasSingleBean(RunAsManager.class));
  }

  @Configuration
  @EnableIglooAutoConfiguration(
      exclude = {
        IglooBootstrap4AutoConfiguration.class,
        IglooApplicationConfigAutoConfiguration.class
      })
  public static class TestConfig {}
}
