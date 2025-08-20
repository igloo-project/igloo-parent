package basicapp.back.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class BasicApplicationBackNotificationConfiguration {

  @Bean
  public FreeMarkerConfigurationFactoryBean freemarkerMailConfiguration() {
    FreeMarkerConfigurationFactoryBean configuration = new FreeMarkerConfigurationFactoryBean();
    configuration.setTemplateLoaderPath("classpath:notification");
    return configuration;
  }
}
