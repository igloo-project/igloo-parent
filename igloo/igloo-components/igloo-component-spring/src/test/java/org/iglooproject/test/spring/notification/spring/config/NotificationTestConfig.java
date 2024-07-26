package org.iglooproject.test.spring.notification.spring.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class NotificationTestConfig {

  @Bean
  @Primary
  public JavaMailSender javaMailSenderMock() {
    return Mockito.mock(JavaMailSender.class);
  }

  @Bean
  public FreeMarkerConfigurationFactoryBean freemarkerMailConfiguration() {
    FreeMarkerConfigurationFactoryBean configuration = new FreeMarkerConfigurationFactoryBean();
    configuration.setTemplateLoaderPath("classpath:notification");
    return configuration;
  }
}
