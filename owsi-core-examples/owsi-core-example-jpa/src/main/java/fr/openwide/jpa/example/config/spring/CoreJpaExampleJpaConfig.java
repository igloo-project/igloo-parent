package fr.openwide.jpa.example.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import fr.openwide.core.jpa.config.spring.AbstractConfiguredJpaConfig;
import fr.openwide.core.jpa.config.spring.DefaultJpaConfig;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.jpa.example.business.CoreJpaExampleBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
@Import(DefaultJpaConfig.class)
public class CoreJpaExampleJpaConfig extends AbstractConfiguredJpaConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(CoreJpaExampleBusinessPackage.class.getPackage());
	}

}
