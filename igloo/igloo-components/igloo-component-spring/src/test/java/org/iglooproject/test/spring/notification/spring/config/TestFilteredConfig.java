package org.iglooproject.test.spring.notification.spring.config;

import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.springframework.context.annotation.Configuration;

/**
 * Load configuration for mail filtering.
 */
@Configuration
@ConfigurationLocations(locations = {"classpath:notification-test-filtered.properties"}, order = Integer.MAX_VALUE)
public class TestFilteredConfig {
}
