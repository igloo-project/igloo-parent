package fr.openwide.core.test.jpa.more.config.spring;

import javax.persistence.spi.PersistenceProvider;

import org.hibernate.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.hibernate.ejb.InterceptorAwareHibernatePersistenceProvider;
import fr.openwide.core.jpa.hibernate.interceptor.ChainedInterceptor;
import fr.openwide.core.jpa.more.config.spring.AbstractConfiguredJpaMoreJpaConfig;
import fr.openwide.core.test.jpa.more.business.JpaMoreTestBusinessPackage;
import fr.openwide.core.test.jpa.more.business.interceptor.TestEntityInterceptor;
import fr.openwide.core.test.jpa.more.business.interceptor.TestEntitySimplePropertyUpdateInterceptor;

@Configuration
@EnableAspectJAutoProxy
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
