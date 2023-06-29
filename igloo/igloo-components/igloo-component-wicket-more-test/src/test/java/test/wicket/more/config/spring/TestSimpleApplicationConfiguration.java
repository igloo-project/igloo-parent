package test.wicket.more.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import test.wicket.more.application.WicketMoreTestApplication;

@Configuration
public class TestSimpleApplicationConfiguration {

	@Bean
	public WebApplication application() {
		return new WicketMoreTestApplication();
	}

}
