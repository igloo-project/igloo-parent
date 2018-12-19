package test.web.config.spring;

import org.iglooproject.basicapp.web.application.config.spring.BasicApplicationWebappConfig;
import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.iglooproject.spring.util.context.ApplicationContextUtils;
import org.iglooproject.wicket.more.config.spring.WicketMoreApplicationPropertyRegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConfigurationLocations(locations = { "classpath:configuration-env-test.properties" }, order = 1000)
@Import({
	BasicApplicationWebappConfig.class,
	WicketMoreApplicationPropertyRegistryConfig.class
})
public class BasicApplicationWebappTestCommonConfig {

//	@Bean
//	public WebApplication application() {
//		return new BasicApplicationApplication();
//	}

	@Bean
	public ApplicationContextUtils applicationContextUtils() {
		return ApplicationContextUtils.getInstance();
	};

}
