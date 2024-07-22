package org.iglooproject.test.config.bootstrap.spring.util;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.test.config.bootstrap.spring.util.locations.SpringWithConfigurationLocationsDefaultConfig;
import org.iglooproject.test.config.bootstrap.spring.util.locations.SpringWithConfigurationLocationsOverrideConfig;
import org.iglooproject.test.config.bootstrap.spring.util.locations.SpringWithConfigurationLocationsPlaceholderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
    name = IglooPropertySourcePriority.APPLICATION,
    value = {
      "classpath:configuration-app.properties",
      "classpath:configuration-app-${igloo.applicationName}.properties",
      "classpath:configuration-app-${user.name}.properties",
      "classpath:configuration-app-placeholder.properties",
    },
    encoding = "UTF-8")
@Import({
  SpringWithConfigurationLocationsOverrideConfig.class,
  SpringWithConfigurationLocationsDefaultConfig.class,
  SpringWithConfigurationLocationsPlaceholderConfig.class
})
public class SpringWithConfigurationLocationsConfig {}
