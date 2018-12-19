package test.web.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BasicApplicationWebappTestCommonConfig.class})
public class BasicApplicationWebappTestWicketTesterConfig {

	@Bean
	public WebApplication application() {
		return new BasicApplicationApplication();
	}

}
