package fr.openwide.core.jpa.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.openwide.core.jpa.config.spring.provider.DatabaseConnectionPoolConfigurationProvider;
import fr.openwide.core.jpa.config.spring.provider.DefaultJpaConfigurationProvider;

@Configuration
public class DefaultJpaConfig {

	@Bean
	public DefaultJpaConfigurationProvider defaultJpaCoreConfigurationProvider() {
		return new DefaultJpaConfigurationProvider();
	}

	@Bean
	public DatabaseConnectionPoolConfigurationProvider defaultDatabaseConnectionPoolConfigurationProvider() {
		return new DatabaseConnectionPoolConfigurationProvider();
	}

}
