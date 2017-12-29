package org.iglooproject.test.jpa.more.config.spring;

import javax.persistence.spi.PersistenceProvider;

import org.hibernate.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.hibernate.ejb.InterceptorAwareHibernatePersistenceProvider;
import org.iglooproject.jpa.hibernate.interceptor.ChainedInterceptor;
import org.iglooproject.jpa.more.config.spring.AbstractConfiguredJpaMoreJpaConfig;
import org.iglooproject.test.jpa.more.business.JpaMoreTestBusinessPackage;
import org.iglooproject.test.jpa.more.business.interceptor.TestEntityInterceptor;
import org.iglooproject.test.jpa.more.business.interceptor.TestEntitySimplePropertyUpdateInterceptor;
import org.iglooproject.test.jpa.more.business.util.transaction.service.ITestTransactionSynchronizationTaskService;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackageClasses = ITestTransactionSynchronizationTaskService.class)
public class JpaMoreTestJpaConfig extends AbstractConfiguredJpaMoreJpaConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(JpaMoreTestBusinessPackage.class.getPackage());
	}
	
	@Bean
	public PersistenceProvider persistenceProvider() {
		return new InterceptorAwareHibernatePersistenceProvider();
	}
	
	@Bean
	public Interceptor interceptor() {
		return new ChainedInterceptor()
				.add(new TestEntityInterceptor())
				.add(new TestEntitySimplePropertyUpdateInterceptor());
	}

}
