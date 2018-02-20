package org.iglooproject.test.config.bootstrap.spring.util;

import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.iglooproject.test.config.bootstrap.spring.util.locations.SpringWithConfigurationLocationsDefaultConfig;
import org.iglooproject.test.config.bootstrap.spring.util.locations.SpringWithConfigurationLocationsOverrideConfig;
import org.iglooproject.test.config.bootstrap.spring.util.locations.SpringWithConfigurationLocationsPlaceholderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>Order 0, between default and overrides.</p>
 * 
 * <p>Import other {@link ConfigurationLocations} beans.</p>
 */
@Configuration
@ConfigurationLocations(
	locations = {
		"classpath:configuration-app.properties",
		"classpath:configuration-app-${igloo.applicationName}.properties",
		"classpath:configuration-app-${user.name}.properties",
		"classpath:configuration-app-placeholder.properties",
	},
	order = 0
)
@Import({
	SpringWithConfigurationLocationsOverrideConfig.class,
	SpringWithConfigurationLocationsDefaultConfig.class,
	SpringWithConfigurationLocationsPlaceholderConfig.class
})
public class SpringWithConfigurationLocationsConfig {

}
