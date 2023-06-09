package test.web.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.config.spring.BasicApplicationWebappConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	BasicApplicationWebappConfig.class,
})
public class BasicApplicationWebappTestCommonConfig {

	@Bean
	public WebApplication application() {
		return new BasicApplicationApplication();
	}

}
