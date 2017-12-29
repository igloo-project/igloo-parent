package org.iglooproject.showcase.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.security.config.spring.AbstractConfiguredJpaSecurityJpaConfig;
import org.iglooproject.showcase.core.business.ShowcaseCoreBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
public class ShowcaseCoreJpaConfig extends AbstractConfiguredJpaSecurityJpaConfig {

	/**
	 * Déclaration des packages de scan pour l'application.
	 */
	@Override
	@Bean
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(ShowcaseCoreBusinessPackage.class.getPackage());
	}

}
