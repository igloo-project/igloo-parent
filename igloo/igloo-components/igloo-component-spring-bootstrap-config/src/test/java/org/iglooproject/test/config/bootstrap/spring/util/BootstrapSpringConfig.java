package org.iglooproject.test.config.bootstrap.spring.util;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.config.bootstrap.spring.ExtendedTestApplicationContextInitializer;
import org.iglooproject.config.bootstrap.spring.config.BootstrapSpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Minimal configuration with initializer setting to enable bootstrap mechanism.
 * 
 * @see ExtendedApplicationContextInitializer
 * @see ExtendedTestApplicationContextInitializer
 */
@Configuration
@Import(BootstrapSpringConfiguration.class)
public class BootstrapSpringConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer configurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
