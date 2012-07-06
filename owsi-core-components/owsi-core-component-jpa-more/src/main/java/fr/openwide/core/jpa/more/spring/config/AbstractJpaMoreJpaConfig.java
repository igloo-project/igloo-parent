package fr.openwide.core.jpa.more.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import fr.openwide.core.jpa.config.spring.AbstractJpaConfig;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.more.business.JpaMoreBusinessPackage;

@ComponentScan(basePackageClasses = { JpaMoreBusinessPackage.class })
public abstract class AbstractJpaMoreJpaConfig extends AbstractJpaConfig {

	@Bean
	public JpaPackageScanProvider jpaMorePackageScanProvider() {
		return new JpaPackageScanProvider(JpaMoreBusinessPackage.class.getPackage());
	}

}
