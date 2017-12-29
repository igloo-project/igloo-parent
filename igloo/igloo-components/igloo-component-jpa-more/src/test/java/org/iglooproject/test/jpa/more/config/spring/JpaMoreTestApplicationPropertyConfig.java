package org.iglooproject.test.jpa.more.config.spring;

import org.springframework.context.annotation.Configuration;

import org.iglooproject.jpa.more.business.parameter.dao.ParameterDaoImpl;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;

@Configuration
public class JpaMoreTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	protected void register(IPropertyRegistry registry) {
	}

	@Override
	public IMutablePropertyDao mutablePropertyDao() {
		return new ParameterDaoImpl();
	}

}
