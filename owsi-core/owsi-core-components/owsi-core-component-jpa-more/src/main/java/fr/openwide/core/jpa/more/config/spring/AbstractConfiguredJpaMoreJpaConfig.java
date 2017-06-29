package fr.openwide.core.jpa.more.config.spring;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import fr.openwide.core.jpa.config.spring.DefaultJpaConfig;
import fr.openwide.core.jpa.config.spring.JpaConfigUtils;

@Import(DefaultJpaConfig.class)
public abstract class AbstractConfiguredJpaMoreJpaConfig extends AbstractJpaMoreJpaConfig {

	@Override
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		return JpaConfigUtils.entityManagerFactory(defaultJpaConfig.defaultJpaCoreConfigurationProvider());
	}

	/**
	 * Déclaration explicite de close comme destroyMethod (Spring doit la prendre en compte auto-magiquement même
	 * si non configurée).
	 */
	@Override
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		return JpaConfigUtils.dataSource(defaultJpaConfig.defaultDatabaseConnectionPoolConfigurationProvider());
	}

}
