package fr.openwide.core.jpa.config.spring;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * Configuration JPA qui se construit automatiquement à partir des clés de configuration par défaut
 * (voir {@link DefaultJpaConfig})
 */
@Import(DefaultJpaConfig.class)
public abstract class AbstractConfiguredJpaConfig extends AbstractJpaConfig {

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
