package org.iglooproject.basicapp.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.iglooproject.basicapp.core.business.BasicApplicationCoreCommonBusinessPackage;
import org.iglooproject.basicapp.core.config.hibernate.HibernateConfigPackage;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.security.config.spring.AbstractConfiguredJpaSecurityJpaConfig;

@Configuration
@EnableAspectJAutoProxy
public class BasicApplicationCoreCommonJpaConfig extends AbstractConfiguredJpaSecurityJpaConfig {

	/**
	 * Déclaration des packages de scan pour l'application.
	 */
	@Override
	@Bean
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(
				BasicApplicationCoreCommonBusinessPackage.class.getPackage(),
				HibernateConfigPackage.class.getPackage() // Typedef config
		);
	}
}
