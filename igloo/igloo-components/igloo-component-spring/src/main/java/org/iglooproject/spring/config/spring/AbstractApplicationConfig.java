package org.iglooproject.spring.config.spring;

import org.iglooproject.spring.config.CorePropertyPlaceholderConfigurer;
import org.iglooproject.spring.config.spring.annotation.CoreConfigurationLocationsAnnotationConfig;
import org.iglooproject.spring.util.ConfigurationLogger;
import org.iglooproject.spring.util.PropertySourceLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Classe à implémenter avec une méthode statique fournissant un {@link PropertySourcesPlaceholderConfigurer} vide.
 * L'annotation {@link ApplicationDescription} permet de configurer le nom de l'application.
 * 
 * L'annotation {@link ConfigurationLocations} est dépréciée en faveur de l'annotation standard {@link PropertySource}
 * 
 * @see ApplicationDescription
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
	public static CorePropertyPlaceholderConfigurer corePropertyPlaceholderConfigurer(ConfigurableApplicationContext context) {
		return new CorePropertyPlaceholderConfigurer();
	}

}
