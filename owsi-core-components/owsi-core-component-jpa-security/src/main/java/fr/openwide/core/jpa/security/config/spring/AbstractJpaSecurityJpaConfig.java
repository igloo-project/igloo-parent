package fr.openwide.core.jpa.security.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.more.config.spring.AbstractJpaMoreJpaConfig;
import fr.openwide.core.jpa.security.business.JpaSecurityBusinessPackage;
import fr.openwide.core.jpa.security.service.JpaSecurityServicePackage;

@ComponentScan(basePackageClasses = {
		JpaSecurityBusinessPackage.class,
		JpaSecurityServicePackage.class
})
public abstract class AbstractJpaSecurityJpaConfig extends AbstractJpaMoreJpaConfig {

	@Bean
	public JpaPackageScanProvider jpaSecurityPackageScanProvider() {
		return new JpaPackageScanProvider(JpaSecurityBusinessPackage.class.getPackage());
	}

}
