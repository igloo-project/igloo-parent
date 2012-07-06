package fr.openwide.core.jpa.security.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.more.spring.config.AbstractJpaMoreJpaConfig;
import fr.openwide.core.jpa.security.acl.JpaSecurityAclPackage;
import fr.openwide.core.jpa.security.business.JpaSecurityBusinessPackage;
import fr.openwide.core.jpa.security.service.JpaSecurityServicePackage;

@ComponentScan(basePackageClasses = {
		JpaSecurityAclPackage.class,
		JpaSecurityBusinessPackage.class,
		JpaSecurityServicePackage.class
})
public abstract class AbstractJpaSecurityJpaConfig extends AbstractJpaMoreJpaConfig {

	@Bean
	public JpaPackageScanProvider jpaSecurityPackageScanProvider() {
		return new JpaPackageScanProvider(JpaSecurityBusinessPackage.class.getPackage());
	}

}
