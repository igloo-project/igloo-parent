package org.iglooproject.test.jpa.externallinkchecker.config.spring;

import org.hibernate.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.iglooproject.jpa.config.spring.AbstractConfiguredJpaConfig;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.externallinkchecker.business.JpaExternalLinkCheckerBusinessPackage;
import org.iglooproject.jpa.externallinkchecker.business.interceptor.ExternalLinkWrapperInterceptor;
import org.iglooproject.jpa.hibernate.interceptor.ChainedInterceptor;

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
