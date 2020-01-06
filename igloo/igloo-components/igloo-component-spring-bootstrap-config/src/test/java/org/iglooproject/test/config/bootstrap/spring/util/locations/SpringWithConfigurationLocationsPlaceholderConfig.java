package org.iglooproject.test.config.bootstrap.spring.util.locations;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
	name = IglooPropertySourcePriority.APPLICATION,
	value = "classpath:configuration-app-placeholder.properties"
)
public class SpringWithConfigurationLocationsPlaceholderConfig {

}
