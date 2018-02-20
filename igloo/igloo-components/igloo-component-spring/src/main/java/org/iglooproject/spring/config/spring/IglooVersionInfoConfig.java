package org.iglooproject.spring.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.springframework.context.annotation.Configuration;

@Configuration
@ManifestPropertySource(prefix = "igloo.component-spring")
@ConfigurationLocations(locations = {
		"classpath:igloo-component-spring.properties"
})
public class IglooVersionInfoConfig {

}
