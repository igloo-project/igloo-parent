package org.igloo.spring.autoconfigure.applicationconfig;

import java.util.Collection;
import java.util.Collections;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.iglooproject.spring.config.spring.IglooVersionInfoConfig;
import org.iglooproject.spring.config.spring.SpringApplicationPropertyRegistryConfig;
import org.iglooproject.spring.config.spring.annotation.CoreConfigurationLocationsAnnotationConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(
    name = "igloo-ac.application.disabled",
    havingValue = "false",
    matchIfMissing = true)
@AutoConfigureAfter(IglooPropertyAutoConfiguration.class)
@Import({
  CoreConfigurationLocationsAnnotationConfig.class,
  // load informations about igloo version and builder
  IglooVersionInfoConfig.class,
  SpringApplicationPropertyRegistryConfig.class
})
public class IglooApplicationConfigAutoConfiguration {

  /**
   * Workaround for Wicket autowired bean of List : encounter an issue if only one bean of List is
   * present
   */
  @Bean
  public Collection<Object> wicketAutowiredWorkaround1() {
    return Collections.emptyList();
  }

  @Bean
  public Collection<Object> wicketAutowiredWorkaround2() {
    return Collections.emptyList();
  }
}
