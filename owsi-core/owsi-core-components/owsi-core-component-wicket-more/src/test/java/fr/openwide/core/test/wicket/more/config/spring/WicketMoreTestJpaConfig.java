package fr.openwide.core.test.wicket.more.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import fr.openwide.core.jpa.config.spring.AbstractConfiguredJpaConfig;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.test.wicket.more.business.WicketMoreTestBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
public class WicketMoreTestJpaConfig extends AbstractConfiguredJpaConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(WicketMoreTestBusinessPackage.class.getPackage());
	}

}
