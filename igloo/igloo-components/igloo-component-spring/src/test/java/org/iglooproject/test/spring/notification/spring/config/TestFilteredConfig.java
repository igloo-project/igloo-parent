package org.iglooproject.test.spring.notification.spring.config;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Load configuration for mail filtering.
 */
@Configuration
@PropertySource(
	name = IglooPropertySourcePriority.APPLICATION,
	value = "classpath:notification-test-filtered.properties",
	encoding = "UTF-8"
)
public class TestFilteredConfig {

}
