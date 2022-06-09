package org.igloo.spring.autoconfigure.bootstrap;

import org.igloo.spring.autoconfigure.wicket.IglooWicketAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import igloo.bootstrap5.application.WicketBootstrap5Module;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.bootstrap5.disabled", havingValue = "false", matchIfMissing = true)
@ConditionalOnClass(value = WicketBootstrap5Module.class)
@AutoConfigureAfter({ IglooWicketAutoConfiguration.class })
public class IglooBootstrap5AutoConfiguration {
	@Bean
	public WicketBootstrap5Module bootstrap5Module() {
		return new WicketBootstrap5Module();
	}
}
