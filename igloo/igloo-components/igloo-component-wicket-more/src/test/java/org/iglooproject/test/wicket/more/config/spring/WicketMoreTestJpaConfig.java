package org.iglooproject.test.wicket.more.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.iglooproject.jpa.config.spring.AbstractConfiguredJpaConfig;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.test.wicket.more.business.WicketMoreTestBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
public class WicketMoreTestJpaConfig extends AbstractConfiguredJpaConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(WicketMoreTestBusinessPackage.class.getPackage());
	}

}
