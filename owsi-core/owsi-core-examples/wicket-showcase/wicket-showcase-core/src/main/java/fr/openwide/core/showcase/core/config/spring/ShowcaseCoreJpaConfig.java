package fr.openwide.core.showcase.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.security.config.spring.AbstractConfiguredJpaSecurityJpaConfig;
import fr.openwide.core.showcase.core.business.ShowcaseCoreBusinessPackage;

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
