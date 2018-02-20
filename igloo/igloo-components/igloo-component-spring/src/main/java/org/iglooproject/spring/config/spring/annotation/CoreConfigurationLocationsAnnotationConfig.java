package org.iglooproject.spring.config.spring.annotation;

import org.iglooproject.config.bootstrap.spring.ApplicationConfigurerBeanFactoryPostProcessor;
import org.iglooproject.config.bootstrap.spring.annotations.ApplicationDescription;
import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.iglooproject.config.bootstrap.spring.config.BootstrapSpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Active la prise en compte des annotations {@link ApplicationDescription} et {@link ConfigurationLocations}.
 * 
 * @see ApplicationConfigurerBeanFactoryPostProcessor
 */
@Configuration
@ConfigurationLocations(locations = {
		"classpath:igloo-component-spring.properties"
})
@Import(BootstrapSpringConfiguration.class)
public class CoreConfigurationLocationsAnnotationConfig {

}
