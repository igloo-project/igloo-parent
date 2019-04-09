package org.igloo.spring.autoconfigure.applicationconfig;

import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.iglooproject.spring.config.spring.IglooVersionInfoConfig;
import org.iglooproject.spring.config.spring.SpringApplicationPropertyRegistryConfig;
import org.iglooproject.spring.config.spring.annotation.CoreConfigurationLocationsAnnotationConfig;
import org.iglooproject.spring.util.ConfigurationLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
	CoreConfigurationLocationsAnnotationConfig.class,
	// load informations about igloo version and builder
	IglooVersionInfoConfig.class,
	SpringApplicationPropertyRegistryConfig.class
})
@AutoConfigureAfter(IglooPropertyAutoConfiguration.class)
@Configuration
public class IglooApplicationConfigAutoConfiguration {

	@Bean
	public ConfigurationLogger configurationLogger(@Value("${propertyNamesForInfoLogLevel}") String propertyNamesForInfoLogLevel) {
		ConfigurationLogger configurationLogger = new ConfigurationLogger();
		
		configurationLogger.setPropertyNamesForInfoLogLevel(propertyNamesForInfoLogLevel);
		
		return configurationLogger;
	}

}
