package org.iglooproject.spring.config.spring.annotation;

import org.iglooproject.config.bootstrap.spring.ApplicationConfigurerBeanFactoryPostProcessor;
import org.iglooproject.config.bootstrap.spring.annotations.ApplicationDescription;
import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Active la prise en compte des annotations {@link ApplicationDescription} et {@link ConfigurationLocations}.
 * 
 * @see ApplicationConfigurerBeanFactoryPostProcessor
 */
@Configuration
@ConfigurationLocations(locations = {
		"classpath:igloo-component-spring.properties"
})
public class CoreConfigurationLocationsAnnotationConfig {

	@Bean
	public static ApplicationConfigurerBeanFactoryPostProcessor applicationConfigurer() {
		return new ApplicationConfigurerBeanFactoryPostProcessor(false);
	}

}
