package org.iglooproject.basicapp.core.test.config.spring;

import org.iglooproject.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	BasicApplicationCoreCommonConfig.class,
})
public class BasicApplicationCoreTestCommonConfig {
	
	@Bean
	public IRendererService rendererService() {
		return new EmptyRendererServiceImpl();
	}

}
