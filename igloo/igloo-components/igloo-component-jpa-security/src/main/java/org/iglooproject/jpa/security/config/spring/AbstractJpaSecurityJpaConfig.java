package org.iglooproject.jpa.security.config.spring;

import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.more.config.spring.AbstractJpaMoreJpaConfig;
import org.iglooproject.jpa.security.business.JpaSecurityBusinessPackage;
import org.iglooproject.jpa.security.service.JpaSecurityServicePackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(
    basePackageClasses = {JpaSecurityBusinessPackage.class, JpaSecurityServicePackage.class})
public abstract class AbstractJpaSecurityJpaConfig extends AbstractJpaMoreJpaConfig {

  @Bean
  public JpaPackageScanProvider jpaSecurityPackageScanProvider() {
    return new JpaPackageScanProvider(JpaSecurityBusinessPackage.class.getPackage());
  }
}
