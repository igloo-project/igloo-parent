package org.iglooproject.jpa.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.config.spring.provider.DatabaseConnectionJndiConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.DatabaseConnectionPoolConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.DatasourceProvider;
import org.iglooproject.jpa.config.spring.provider.DefaultJpaConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.IDatabaseConnectionConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
@PropertySource(
	name = IglooPropertySourcePriority.COMPONENT,
	value = "classpath:igloo-component-jpa.properties",
	encoding = "UTF-8"
)
public class DefaultJpaConfig {

	public static final Logger LOGGER = LoggerFactory.getLogger(DefaultJpaConfig.class);

	@Bean
	@Scope(proxyMode = ScopedProxyMode.INTERFACES)
	public IJpaConfigurationProvider defaultJpaCoreConfigurationProvider() {
		return new DefaultJpaConfigurationProvider();
	}

	@Bean
	public IDatabaseConnectionConfigurationProvider defaultDatabaseConnectionPoolConfigurationProvider(
			@Value("${db.datasourceProvider:}") DatasourceProvider datasourceProvider) {
		if (DatasourceProvider.JNDI.equals(datasourceProvider)) {
			LOGGER.info("JDBC pool configuration: using jndi");
			return new DatabaseConnectionJndiConfigurationProvider();
		} else if (DatasourceProvider.APPLICATION.equals(datasourceProvider)) {
			LOGGER.info("JDBC pool configuration: using application pool");
			return new DatabaseConnectionPoolConfigurationProvider();
		} else {
			throw new IllegalStateException(String.format("JDBC pool configuration: unknown provider %s",
					datasourceProvider.name()));
		}
	}

}
