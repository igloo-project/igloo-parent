package org.iglooproject.config.bootstrap.autoconfigure;

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
public class ManifestAutoConfiguration {

  public ManifestAutoConfiguration() { // NOSONAR
    // nothing, constructor is needed by spring despite this class has no method
  }

  @Bean
  public static ManifestPropertySourceBeanFactoryPostProcessor manifestPropertySourceConfigurer() {
    return new ManifestPropertySourceBeanFactoryPostProcessor();
  }
}
