package org.iglooproject.basicapp.core.config.spring;

import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.ENVIRONMENT;
import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS;
import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_VALIDATOR_ENABLED;

import org.iglooproject.basicapp.core.config.util.Environment;
import org.iglooproject.commons.util.functional.Suppliers2;
import org.iglooproject.commons.util.functional.converter.StringCollectionConverter;
import org.iglooproject.jpa.more.business.parameter.dao.ParameterDaoImpl;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;

@Configuration
public class BasicApplicationCoreApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerEnum(ENVIRONMENT, Environment.class, Environment.production);
		
		registry.registerBoolean(SECURITY_PASSWORD_VALIDATOR_ENABLED, true);
		registry.register(
				SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS,
				new StringCollectionConverter<>(
						Converter.<String>identity(),
						Suppliers2.<String>arrayList()
				),
				Lists.<String>newArrayList()
		);
	}

	@Override
	public IMutablePropertyDao mutablePropertyDao() {
		return new ParameterDaoImpl();
	}

}
