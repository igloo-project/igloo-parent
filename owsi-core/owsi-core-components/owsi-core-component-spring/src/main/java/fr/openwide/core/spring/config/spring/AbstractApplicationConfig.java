package fr.openwide.core.spring.config.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import fr.openwide.core.spring.config.spring.annotation.ApplicationDescription;
import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;
import fr.openwide.core.spring.config.spring.annotation.CoreConfigurationLocationsAnnotationConfig;
import fr.openwide.core.spring.util.ConfigurationLogger;

/**
 * Classe à implémenter avec une méthode statique fournissant un {@link PropertySourcesPlaceholderConfigurer} vide.
 * Les annotations {@link ApplicationDescription} et {@link ConfigurationLocations} permettent de charger la
 * configuration.
 * 
 * @see ApplicationDescription
 * @see ConfigurationLocations
 */
@Import(CoreConfigurationLocationsAnnotationConfig.class)
public abstract class AbstractApplicationConfig {

	@Bean
	public ConfigurationLogger configurationLogger(@Value("${propertyNamesForInfoLogLevel}") String propertyNamesForInfoLogLevel) {
		ConfigurationLogger configurationLogger = new ConfigurationLogger();
		
		configurationLogger.setPropertyNamesForInfoLogLevel(propertyNamesForInfoLogLevel);
		
		return configurationLogger;
	}

}
