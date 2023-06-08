package org.igloo.spring.autoconfigure.bootstrap;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import igloo.bootstrap4.application.WicketBootstrap4Module;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.bootstrap4.disabled", havingValue = "false", matchIfMissing = true)
@ConditionalOnClass(value = WicketBootstrap4Module.class)
@ConditionalOnBean(WebApplication.class)
public class IglooBootstrap4AutoConfiguration {
	@Bean
	public WicketBootstrap4Module bootstrap4Module() {
		return new WicketBootstrap4Module();
	}
}
