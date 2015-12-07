package fr.openwide.core.test.jpa.externallinkchecker.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import fr.openwide.core.jpa.config.spring.AbstractConfiguredJpaConfig;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.externallinkchecker.business.JpaExternalLinkCheckerBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
public class JpaExternalLinkCheckerTestJpaConfig extends AbstractConfiguredJpaConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(JpaExternalLinkCheckerBusinessPackage.class.getPackage());
	}

}
