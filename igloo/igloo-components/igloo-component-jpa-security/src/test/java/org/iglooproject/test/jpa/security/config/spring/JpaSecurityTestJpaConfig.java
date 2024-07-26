package org.iglooproject.test.jpa.security.config.spring;

import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.security.config.spring.AbstractConfiguredJpaSecurityJpaConfig;
import org.iglooproject.test.jpa.security.business.JpaSecurityTestBusinessPackage;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class JpaSecurityTestJpaConfig extends AbstractConfiguredJpaSecurityJpaConfig {

  @Override
  public JpaPackageScanProvider applicationJpaPackageScanProvider() {
    return new JpaPackageScanProvider(JpaSecurityTestBusinessPackage.class.getPackage());
  }
}
