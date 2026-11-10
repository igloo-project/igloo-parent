package org.iglooproject.spring.autoconfigure;

import org.iglooproject.commons.util.mime.IMediaTypeRegistry;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.commons.util.mime.MediaTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MediaTypeAutoConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(MediaTypeAutoConfiguration.class);

  @Bean
  @ConditionalOnMissingBean
  public IMediaTypeRegistry mediaTypeRegistry() {
    return MediaTypeRegistry.of(MediaType.class);
  }

  @Bean
  public SmartInitializingSingleton mediaTypeRegistryOverridesLogger(
      IMediaTypeRegistry mediaTypeRegistry) {
    return () ->
        mediaTypeRegistry
            .overrides()
            .forEach(
                (overridden, override) ->
                    LOGGER.info(
                        "Media type {} {} overridden by {}.{} {}",
                        overridden,
                        overridden.mime(),
                        override.getClass().getSimpleName(),
                        override,
                        override.definition()));
  }
}
