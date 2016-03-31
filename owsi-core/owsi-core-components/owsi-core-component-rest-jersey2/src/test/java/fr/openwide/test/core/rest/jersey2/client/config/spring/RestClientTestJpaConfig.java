package fr.openwide.test.core.rest.jersey2.client.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import fr.openwide.core.jpa.config.spring.AbstractConfiguredJpaConfig;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.test.core.rest.jersey2.business.RestTestBusinessPackage;

@Configuration
@EnableAspectJAutoProxy
public class RestClientTestJpaConfig extends AbstractConfiguredJpaConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(RestTestBusinessPackage.class.getPackage());
	}

}
