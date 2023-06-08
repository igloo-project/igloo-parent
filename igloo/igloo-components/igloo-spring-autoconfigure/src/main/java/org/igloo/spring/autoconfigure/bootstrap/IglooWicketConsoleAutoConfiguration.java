package org.igloo.spring.autoconfigure.bootstrap;

import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.wicket.more.autoconfigure.WicketMoreAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import igloo.console.WicketConsoleModule;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.bootstrap5.disabled", havingValue = "false", matchIfMissing = true)
@ConditionalOnClass(value = WicketConsoleModule.class)
@AutoConfigureAfter({ WicketMoreAutoConfiguration.class })
@ConditionalOnBean(WebApplication.class)
public class IglooWicketConsoleAutoConfiguration {
	@Bean
	public WicketConsoleModule consoleModule() {
		return new WicketConsoleModule();
	}
}
