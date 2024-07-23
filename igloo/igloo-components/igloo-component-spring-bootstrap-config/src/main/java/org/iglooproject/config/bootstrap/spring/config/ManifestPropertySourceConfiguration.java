package org.iglooproject.config.bootstrap.spring.config;

import org.iglooproject.config.bootstrap.spring.ManifestPropertySourceBeanFactoryPostProcessor;
import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration allow to inject MANIFEST.MF informations as properties.
 *
 * @see ManifestPropertySource
 * @see ManifestPropertySourceBeanFactoryPostProcessor
 */
@Configuration
public class ManifestPropertySourceConfiguration {

  @Bean
  public static ManifestPropertySourceBeanFactoryPostProcessor manifestPropertySourceConfigurer() {
    return new ManifestPropertySourceBeanFactoryPostProcessor();
  }
}
