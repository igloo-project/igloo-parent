package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.applicationconfig.IglooApplicationConfigAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.igloo.spring.autoconfigure.security.IglooJpaSecurityAutoConfiguration;
import org.iglooproject.jpa.more.business.task.service.QueuedTaskHolderManagerImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers
 * IglooTaskManagerAutoConfiguration properly.
 *
 * <p>This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 */
class TaskManagementAutoConfigurationTestCase {

  /**
   * Check that autoconfiguration from {@link QueuedTaskHolderManagerImpl} is triggered with
   * EnableIglooAutoConfiguration
   */
  @Test
  void testIglooTaskManagementAutoConfigure() {
    new ApplicationContextRunner()
        .withAllowBeanDefinitionOverriding(true)
        .withConfiguration(AutoConfigurations.of(TestConfig.class))
        .run(context -> assertThat(context).hasSingleBean(QueuedTaskHolderManagerImpl.class));
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
