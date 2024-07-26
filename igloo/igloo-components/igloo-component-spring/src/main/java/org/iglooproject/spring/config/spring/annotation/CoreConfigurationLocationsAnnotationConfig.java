package org.iglooproject.spring.config.spring.annotation;

import org.iglooproject.config.bootstrap.spring.config.ManifestPropertySourceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Active la prise en compte des annotations {@link ApplicationDescription} et {@link
 * ConfigurationLocations} (dépréciée).
 *
 * @see ApplicationConfigurerBeanFactoryPostProcessor
 */
@Configuration
@Import({
  // cleanly separate bootstrap and configuration overrides
  //	BootstrapSpringConfiguration.class,
  // transform MANIFEST.MF files in properties
  ManifestPropertySourceConfiguration.class
})
public class CoreConfigurationLocationsAnnotationConfig {}
