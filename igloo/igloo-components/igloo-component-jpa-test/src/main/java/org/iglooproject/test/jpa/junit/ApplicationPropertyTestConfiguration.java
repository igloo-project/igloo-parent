package org.iglooproject.test.jpa.junit;

import org.iglooproject.spring.config.spring.AbstractApplicationPropertyConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.dao.StubMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Bean;

public class ApplicationPropertyTestConfiguration extends AbstractApplicationPropertyConfig {

	@Override
	@Bean
	public IMutablePropertyDao mutablePropertyDao() {
		return new StubMutablePropertyDao();
	}

	@Override
	public void register(IPropertyRegistry registry) {
	}

}
