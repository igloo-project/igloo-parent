package fr.openwide.core.test.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import fr.openwide.core.jpa.config.spring.AbstractConfiguredJpaConfig;
import fr.openwide.core.jpa.config.spring.DefaultJpaConfig;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.test.jpa.example.business.JpaTestBusinessPackage;

@Configuration
@Import(DefaultJpaConfig.class)
@EnableAspectJAutoProxy
public class JpaTestJpaConfig extends AbstractConfiguredJpaConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(JpaTestBusinessPackage.class.getPackage());
	}

}
