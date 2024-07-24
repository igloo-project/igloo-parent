package org.iglooproject.spring.autoconfigure;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.spring.util.ConfigurationLogger;
import org.iglooproject.spring.util.PropertySourceLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@PropertySource(
    name = IglooPropertySourcePriority.FRAMEWORK,
    value = {"classpath:igloo-component-spring/configuration-logger.properties"},
    encoding = "UTF-8")
public class ConfigurationLoggerAutoConfiguration {

  @Bean
  public ConfigurationLogger configurationLogger(
      @Value("${propertyNamesForInfoLogLevel}") String propertyNamesForInfoLogLevel) {
    ConfigurationLogger configurationLogger = new ConfigurationLogger();

    configurationLogger.setPropertyNamesForInfoLogLevel(propertyNamesForInfoLogLevel);

    return configurationLogger;
  }

  @Bean
  public PropertySourceLogger propertySourceLogger() {
    return new PropertySourceLogger();
  }
}
