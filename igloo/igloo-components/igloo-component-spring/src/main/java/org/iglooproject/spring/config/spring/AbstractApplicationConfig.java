package org.iglooproject.spring.config.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import org.iglooproject.spring.config.CorePropertyPlaceholderConfigurer;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;
import org.iglooproject.spring.config.spring.annotation.CoreConfigurationLocationsAnnotationConfig;
import org.iglooproject.spring.util.ConfigurationLogger;

/**
 * Classe à implémenter avec une méthode statique fournissant un {@link PropertySourcesPlaceholderConfigurer} vide.
 * Les annotations {@link ApplicationDescription} et {@link ConfigurationLocations} permettent de charger la
 * configuration.
 * 
 * @see ApplicationDescription
 * @see ConfigurationLocations
 */
@Import({
	CoreConfigurationLocationsAnnotationConfig.class,
	SpringApplicationPropertyRegistryConfig.class
})
public abstract class AbstractApplicationConfig {

	@Bean
	public ConfigurationLogger configurationLogger(@Value("${propertyNamesForInfoLogLevel}") String propertyNamesForInfoLogLevel) {
		ConfigurationLogger configurationLogger = new ConfigurationLogger();
		
		configurationLogger.setPropertyNamesForInfoLogLevel(propertyNamesForInfoLogLevel);
		
		return configurationLogger;
	}
	
	@Bean
	public static CorePropertyPlaceholderConfigurer environment(ConfigurableApplicationContext context) {
		return new CorePropertyPlaceholderConfigurer();
	}

}
