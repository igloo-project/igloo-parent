package fr.openwide.core.jpa.more.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import fr.openwide.core.jpa.config.spring.AbstractJpaConfig;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.more.business.JpaMoreBusinessPackage;

@ComponentScan(basePackageClasses = { JpaMoreBusinessPackage.class })
public abstract class AbstractJpaMoreJpaConfig extends AbstractJpaConfig {

	@Bean
	public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
		return new TransactionTemplate(transactionManager);
	}

	@Bean
	public JpaPackageScanProvider jpaMorePackageScanProvider() {
		return new JpaPackageScanProvider(JpaMoreBusinessPackage.class.getPackage());
	}

}
