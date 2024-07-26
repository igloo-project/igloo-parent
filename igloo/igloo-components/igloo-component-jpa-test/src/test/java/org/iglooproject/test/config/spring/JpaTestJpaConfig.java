package org.iglooproject.test.config.spring;

import org.iglooproject.jpa.config.spring.AbstractConfiguredJpaConfig;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.test.business.JpaTestBusinessPackage;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class JpaTestJpaConfig extends AbstractConfiguredJpaConfig {

  @Override
  public JpaPackageScanProvider applicationJpaPackageScanProvider() {
    return new JpaPackageScanProvider(JpaTestBusinessPackage.class.getPackage());
  }
}
