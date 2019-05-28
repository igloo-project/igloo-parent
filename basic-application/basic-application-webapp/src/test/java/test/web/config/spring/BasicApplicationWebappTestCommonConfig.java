package test.web.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.config.spring.BasicApplicationWebappConfig;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.wicket.more.config.spring.WicketMoreApplicationPropertyRegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(name = IglooPropertySourcePriority.APPLICATION, value = "classpath:configuration-env-test.properties")
@Import({
	BasicApplicationWebappConfig.class,
	WicketMoreApplicationPropertyRegistryConfig.class
})
public class BasicApplicationWebappTestCommonConfig {

	@Bean
	public WebApplication application() {
		return new BasicApplicationApplication();
	}

}
