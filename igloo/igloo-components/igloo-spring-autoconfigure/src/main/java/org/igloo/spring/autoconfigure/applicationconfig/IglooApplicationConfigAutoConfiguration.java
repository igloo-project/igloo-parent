package org.igloo.spring.autoconfigure.applicationconfig;

import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.iglooproject.spring.config.spring.IglooVersionInfoConfig;
import org.iglooproject.spring.config.spring.SpringApplicationPropertyRegistryConfig;
import org.iglooproject.spring.config.spring.annotation.CoreConfigurationLocationsAnnotationConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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

}
