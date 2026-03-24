package test.core.config;

import basicapp.back.business.notification.service.EmptyNotificationContentDescriptorFactoryImpl;
import basicapp.back.business.notification.service.IBasicApplicationNotificationContentDescriptorFactory;
import basicapp.back.config.BasicApplicationBackBaseConfiguration;
import basicapp.back.config.BasicApplicationBackDifferenceConfiguration;
import basicapp.back.config.BasicApplicationBackJpaConfiguration;
import basicapp.back.config.BasicApplicationBackManifestConfiguration;
import basicapp.back.config.BasicApplicationBackNotificationConfiguration;
import basicapp.back.config.BasicApplicationBackPropertyRegistryConfiguration;
import basicapp.back.config.BasicApplicationBackReferenceDataConfiguration;
import basicapp.back.config.BasicApplicationBackSecurityConfiguration;
import basicapp.back.config.BasicApplicationBackTaskManagementConfiguration;
import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import test.core.TestBackPackage;

@Configuration
@Import({
  BasicApplicationBackBaseConfiguration.class,
  BasicApplicationBackManifestConfiguration.class,
  BasicApplicationBackJpaConfiguration.class,
  BasicApplicationBackPropertyRegistryConfiguration.class,
  BasicApplicationBackSecurityConfiguration.class,
  BasicApplicationBackTaskManagementConfiguration.class,
  BasicApplicationBackNotificationConfiguration.class,
  BasicApplicationBackReferenceDataConfiguration.class,
  BasicApplicationBackDifferenceConfiguration.class,
  BasicApplicationBackTestBaseConfiguration.class,
  PsqlTestContainerConfiguration.class
})
@ComponentScan(basePackageClasses = TestBackPackage.class)
public class BasicApplicationBackSpringBootTestConfiguration {

  @Bean
  public IRendererService rendererService() {
    return new EmptyRendererServiceImpl();
  }

  @Bean
  public IBasicApplicationNotificationContentDescriptorFactory contentDescriptorFactory() {
    return new EmptyNotificationContentDescriptorFactoryImpl();
  }
}
