package fr.openwide.core.basicapp.web.application.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.wicket.more.config.spring.AbstractWebappConfig;

@Configuration
@Import({
	BasicApplicationCoreCommonConfig.class,
	BasicApplicationWebappSecurityConfig.class,
	BasicApplicationWebappCacheConfig.class,
})
public class BasicApplicationWebappConfig extends AbstractWebappConfig {

	@Bean(name= { "basic-applicationApplication", "application" })
	public BasicApplicationApplication application() {
		return new BasicApplicationApplication();
	}
}
