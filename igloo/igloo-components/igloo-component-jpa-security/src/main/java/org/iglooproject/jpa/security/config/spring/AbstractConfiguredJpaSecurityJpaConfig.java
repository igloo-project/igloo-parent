package org.iglooproject.jpa.security.config.spring;

import org.iglooproject.jpa.config.spring.DefaultJpaConfig;
import org.iglooproject.jpa.config.spring.JpaApplicationPropertyRegistryConfig;
import org.iglooproject.jpa.config.spring.JpaConfigUtils;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Import({DefaultJpaConfig.class, JpaApplicationPropertyRegistryConfig.class})
public abstract class AbstractConfiguredJpaSecurityJpaConfig extends AbstractJpaSecurityJpaConfig {

  @Override
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    return JpaConfigUtils.entityManagerFactory(jpaConfigurationProvider);
  }
}
