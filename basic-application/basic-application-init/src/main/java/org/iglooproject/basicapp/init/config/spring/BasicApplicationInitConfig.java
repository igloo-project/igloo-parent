package org.iglooproject.basicapp.init.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.iglooproject.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import org.iglooproject.basicapp.init.BasicApplicationInitPackage;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;

@Configuration
@Import({
	BasicApplicationCoreCommonConfig.class
})
@ConfigurationLocations(
		locations = {
				"classpath:configuration-init.properties",
				"classpath:configuration-init-${user}.properties"
		},
		order = 1 // Permet de surcharger la configuration du core
)
@ComponentScan(
		basePackageClasses = {
			BasicApplicationInitPackage.class
		}
)
public class BasicApplicationInitConfig {
	
	@Bean
	public IRendererService rendererService() {
		return new EmptyRendererServiceImpl();
	}

}
