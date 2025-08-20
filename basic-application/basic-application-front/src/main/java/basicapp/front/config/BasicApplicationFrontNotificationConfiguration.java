package basicapp.front.config;

import basicapp.back.business.notification.service.IBasicApplicationNotificationContentDescriptorFactory;
import basicapp.back.business.notification.service.IBasicApplicationNotificationUrlBuilderService;
import basicapp.front.notification.service.BasicApplicationNotificationContentDescriptorFactoryImpl;
import basicapp.front.notification.service.BasicApplicationNotificationUrlBuilderServiceImpl;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApplicationFrontNotificationConfiguration {

  @Bean
  public IBasicApplicationNotificationContentDescriptorFactory contentDescriptorFactory(
      IWicketContextProvider wicketContextProvider) {
    return new BasicApplicationNotificationContentDescriptorFactoryImpl(wicketContextProvider);
  }

  @Bean
  public IBasicApplicationNotificationUrlBuilderService notificationUrlBuilderService(
      IWicketContextProvider wicketContextProvider) {
    return new BasicApplicationNotificationUrlBuilderServiceImpl(wicketContextProvider);
  }
}
