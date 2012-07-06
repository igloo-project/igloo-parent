package fr.openwide.core.jpa.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.openwide.core.jpa.config.spring.provider.DefaultJpaConfigurationProvider;
import fr.openwide.core.jpa.config.spring.provider.DefaultTomcatPoolConfigurationProvider;

@Configuration
public class DefaultJpaConfig {

	@Bean
	public DefaultJpaConfigurationProvider defaultJpaCoreConfigurationProvider() {
		return new DefaultJpaConfigurationProvider();
	}

	@Bean
	public DefaultTomcatPoolConfigurationProvider defaultTomcatPoolConfigurationProvider() {
		return new DefaultTomcatPoolConfigurationProvider();
	}

}
