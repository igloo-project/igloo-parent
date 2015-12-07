package fr.openwide.core.test.jpa.externallinkchecker.config.spring;

import org.hibernate.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import fr.openwide.core.jpa.config.spring.AbstractConfiguredJpaConfig;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.externallinkchecker.business.JpaExternalLinkCheckerBusinessPackage;
import fr.openwide.core.jpa.externallinkchecker.business.interceptor.ExternalLinkWrapperInterceptor;
import fr.openwide.core.jpa.hibernate.interceptor.ChainedInterceptor;

@Configuration
@EnableAspectJAutoProxy
public class JpaExternalLinkCheckerTestJpaConfig extends AbstractConfiguredJpaConfig {

	@Override
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(JpaExternalLinkCheckerBusinessPackage.class.getPackage());
	}
	
	@Bean
	public Interceptor interceptor() {
		return new ChainedInterceptor()
				.add(new ExternalLinkWrapperInterceptor());
	}

}
