package fr.openwide.core.basicapp.core.test.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import fr.openwide.core.jpa.more.rendering.service.EmptyRendererServiceImpl;
import fr.openwide.core.jpa.more.rendering.service.IRendererService;
import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;

@Configuration
@ConfigurationLocations(locations = { "classpath:configuration-test.properties" }, order = 1000)
@Import({
	BasicApplicationCoreCommonConfig.class,
})
public class BasicApplicationCoreTestCommonConfig {
	
	@Bean
	public IRendererService rendererService() {
		return new EmptyRendererServiceImpl();
	}

}
