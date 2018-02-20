package org.iglooproject.test.config.bootstrap.spring.util.locations;

import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationLocations(locations = "classpath:configuration-app-placeholder.properties", order = 0)
public class SpringWithConfigurationLocationsPlaceholderConfig {

}
