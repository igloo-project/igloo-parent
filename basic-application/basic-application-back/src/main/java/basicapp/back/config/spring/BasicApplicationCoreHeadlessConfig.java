package basicapp.back.config.spring;

import basicapp.back.BasicApplicationCorePackage;
import basicapp.back.business.notification.service.EmptyNotificationContentDescriptorFactoryImpl;
import basicapp.back.business.notification.service.EmptyNotificationUrlBuilderServiceImpl;
import basicapp.back.business.notification.service.IBasicApplicationNotificationContentDescriptorFactory;
import basicapp.back.business.notification.service.IBasicApplicationNotificationUrlBuilderService;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BasicApplicationCoreCommonConfiguration.class})
@ComponentScan(basePackageClasses = {BasicApplicationCorePackage.class})
public class BasicApplicationCoreHeadlessConfig {

  @Bean
  public IRendererService rendererService() {
    return new EmptyRendererServiceImpl();
  }

  @Bean
  public IBasicApplicationNotificationContentDescriptorFactory contentDescriptorFactory() {
    return new EmptyNotificationContentDescriptorFactoryImpl();
  }

  @Bean
  public IBasicApplicationNotificationUrlBuilderService notificationUrlBuilderService() {
    return new EmptyNotificationUrlBuilderServiceImpl();
  }
}
