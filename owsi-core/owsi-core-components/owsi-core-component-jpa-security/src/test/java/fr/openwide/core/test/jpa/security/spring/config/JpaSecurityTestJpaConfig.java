package fr.openwide.core.test.jpa.security.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.security.spring.config.AbstractConfiguredJpaSecurityConfig;
import fr.openwide.core.test.jpa.security.business.JpaSecurityTestBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
public class JpaSecurityTestJpaConfig extends AbstractConfiguredJpaSecurityConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(JpaSecurityTestBusinessPackage.class.getPackage());
	}

}
