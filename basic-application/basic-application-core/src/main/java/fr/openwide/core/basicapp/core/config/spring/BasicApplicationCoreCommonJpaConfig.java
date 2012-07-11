package fr.openwide.core.basicapp.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import fr.openwide.core.basicapp.core.business.BasicApplicationCoreCommonBusinessPackage;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.security.config.spring.AbstractConfiguredJpaSecurityJpaConfig;

@Configuration
@EnableAspectJAutoProxy
public class BasicApplicationCoreCommonJpaConfig extends AbstractConfiguredJpaSecurityJpaConfig {

	/**
	 * Déclaration des packages de scan pour l'application.
	 */
	@Override
	@Bean
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(BasicApplicationCoreCommonBusinessPackage.class.getPackage());
	}
}
