package org.iglooproject.basicapp.core.config.spring;

import org.iglooproject.basicapp.core.BasicApplicationCorePackage;
import org.iglooproject.basicapp.core.business.notification.service.EmptyNotificationContentDescriptorFactoryImpl;
import org.iglooproject.basicapp.core.business.notification.service.EmptyNotificationUrlBuilderServiceImpl;
import org.iglooproject.basicapp.core.business.notification.service.IBasicApplicationNotificationContentDescriptorFactory;
import org.iglooproject.basicapp.core.business.notification.service.IBasicApplicationNotificationUrlBuilderService;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BasicApplicationCoreCommonConfig.class})
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
