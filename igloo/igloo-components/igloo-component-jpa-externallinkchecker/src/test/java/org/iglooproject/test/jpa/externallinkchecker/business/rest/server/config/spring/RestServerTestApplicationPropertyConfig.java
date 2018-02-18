package org.iglooproject.test.jpa.externallinkchecker.business.rest.server.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.iglooproject.jpa.externallinkchecker.config.spring.JpaExternalLinkCheckerApplicationPropertyRegistryConfig;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.dao.StubMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;

@Import(JpaExternalLinkCheckerApplicationPropertyRegistryConfig.class)
public class RestServerTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	@Bean
	public IMutablePropertyDao mutablePropertyDao() {
		return new StubMutablePropertyDao();
	}

	@Override
	public void register(IPropertyRegistry registry) {
	}

}
