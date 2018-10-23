package org.iglooproject.basicapp.web.test.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.config.spring.BasicApplicationWebappApplicationPropertyRegistryConfig;
import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.iglooproject.wicket.more.config.spring.WicketMoreApplicationPropertyRegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConfigurationLocations(locations = { "classpath:configuration-env-test.properties" }, order = 1000)
@Import({
	BasicApplicationCoreCommonConfig.class,
	BasicApplicationWebappApplicationPropertyRegistryConfig.class,
	WicketMoreApplicationPropertyRegistryConfig.class
})
public class BasicApplicationWebappTestCommonConfig {

	@Bean
	public WebApplication application() {
		return new BasicApplicationApplication();
	}

}
