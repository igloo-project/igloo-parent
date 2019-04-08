package org.iglooproject.jpa.more.config.spring;

import org.iglooproject.jpa.config.spring.DefaultJpaConfig;
import org.iglooproject.jpa.config.spring.JpaApplicationPropertyRegistryConfig;
import org.iglooproject.jpa.config.spring.JpaConfigUtils;
import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Import({ DefaultJpaConfig.class, JpaApplicationPropertyRegistryConfig.class })
public abstract class AbstractConfiguredJpaMoreJpaConfig extends AbstractJpaMoreJpaConfig {
	
	@Autowired
	protected IJpaConfigurationProvider jpaConfigurationProvider;

	@Override
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		return JpaConfigUtils.entityManagerFactory(jpaConfigurationProvider);
	}

}
