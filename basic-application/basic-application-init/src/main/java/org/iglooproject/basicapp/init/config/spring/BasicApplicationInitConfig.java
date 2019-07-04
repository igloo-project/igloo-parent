package org.iglooproject.basicapp.init.config.spring;

import org.iglooproject.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import org.iglooproject.basicapp.init.BasicApplicationInitPackage;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(name = IglooPropertySourcePriority.APPLICATION, value = "classpath:configuration-init.properties")
@Import({
	BasicApplicationCoreCommonConfig.class
})
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
