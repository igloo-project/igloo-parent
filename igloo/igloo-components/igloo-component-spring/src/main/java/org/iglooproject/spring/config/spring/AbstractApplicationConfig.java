package org.iglooproject.spring.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.ApplicationDescription;
import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.iglooproject.spring.config.CorePropertyPlaceholderConfigurer;
import org.iglooproject.spring.config.spring.annotation.CoreConfigurationLocationsAnnotationConfig;
import org.iglooproject.spring.util.ConfigurationLogger;
import org.iglooproject.spring.util.PropertySourceLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

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
	// load informations about igloo version and builder
	IglooVersionInfoConfig.class,
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
	public PropertySourceLogger propertySourceLogger() {
		return new PropertySourceLogger();
	}

	@Bean
	public static CorePropertyPlaceholderConfigurer environment(ConfigurableApplicationContext context) {
		return new CorePropertyPlaceholderConfigurer();
	}

}
