package org.iglooproject.test.config.spring;

import org.springframework.context.annotation.Bean;

import org.iglooproject.spring.config.spring.AbstractApplicationPropertyConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.dao.StubMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;

public class JpaTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	@Bean
	public IMutablePropertyDao mutablePropertyDao() {
		return new StubMutablePropertyDao();
	}

	@Override
	protected void register(IPropertyRegistry registry) {
	}

}
