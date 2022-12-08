package org.igloo.spring.autoconfigure.bootstrap;

import org.igloo.spring.autoconfigure.wicket.IglooWicketAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import igloo.console.WicketConsoleModule;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.bootstrap5.disabled", havingValue = "false", matchIfMissing = true)
@ConditionalOnClass(value = WicketConsoleModule.class)
@AutoConfigureAfter({ IglooWicketAutoConfiguration.class })
public class IglooWicketConsoleAutoConfiguration {
	@Bean
	public WicketConsoleModule consoleModule() {
		return new WicketConsoleModule();
	}
}
