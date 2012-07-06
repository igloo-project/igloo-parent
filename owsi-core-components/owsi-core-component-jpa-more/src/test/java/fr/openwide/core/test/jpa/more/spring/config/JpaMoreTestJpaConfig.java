package fr.openwide.core.test.jpa.more.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import fr.openwide.core.jpa.config.spring.DefaultJpaConfig;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.more.spring.config.AbstractConfiguredJpaMoreConfig;
import fr.openwide.core.test.jpa.more.business.JpaMoreTestBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
@Import(DefaultJpaConfig.class)
public class JpaMoreTestJpaConfig extends AbstractConfiguredJpaMoreConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(JpaMoreTestBusinessPackage.class.getPackage());
	}

}
