package org.iglooproject.test.jpa.security.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.security.config.spring.AbstractConfiguredJpaSecurityJpaConfig;
import org.iglooproject.test.jpa.security.business.JpaSecurityTestBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
public class JpaSecurityTestJpaConfig extends AbstractConfiguredJpaSecurityJpaConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(JpaSecurityTestBusinessPackage.class.getPackage());
	}

}
