package fr.openwide.core.test.jpa.more.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.more.config.spring.AbstractConfiguredJpaMoreJpaConfig;
import fr.openwide.core.test.jpa.more.business.JpaMoreTestBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
public class JpaMoreTestJpaConfig extends AbstractConfiguredJpaMoreJpaConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(JpaMoreTestBusinessPackage.class.getPackage());
	}

}
