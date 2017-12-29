package org.iglooproject.showcase.core.config.spring;

import static org.iglooproject.showcase.core.util.property.ShowcaseCorePropertyIds.SHOWCASE_FILE_ROOT_DIRECTORY;

import org.springframework.context.annotation.Configuration;

import org.iglooproject.jpa.more.business.parameter.dao.ParameterDaoImpl;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;

@Configuration
public class ShowcaseCoreApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerString(SHOWCASE_FILE_ROOT_DIRECTORY);
	}

	@Override
	public IMutablePropertyDao mutablePropertyDao() {
		return new ParameterDaoImpl();
	}

}
