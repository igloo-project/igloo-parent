package org.iglooproject.config.bootstrap.spring.config;

import org.iglooproject.config.bootstrap.spring.AbstractExtendedApplicationContextInitializer;
import org.iglooproject.config.bootstrap.spring.ApplicationConfigurerBeanFactoryPostProcessor;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.config.bootstrap.spring.ExtendedTestApplicationContextInitializer;
import org.iglooproject.config.bootstrap.spring.annotations.ApplicationDescription;
import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration must be combined with a custom {@link AbstractExtendedApplicationContextInitializer} to
 * use bootstrap igloo override profiles, {@link ApplicationDescription}, {@link ConfigurationLocations}
 * and log4j merged configuration.
 * 
 * @see ExtendedTestApplicationContextInitializer
 * @see ExtendedApplicationContextInitializer
 */
@Configuration
public class BootstrapSpringConfiguration {

	@Bean
	public static ApplicationConfigurerBeanFactoryPostProcessor applicationConfigurer() {
		return new ApplicationConfigurerBeanFactoryPostProcessor(false);
	}

}
