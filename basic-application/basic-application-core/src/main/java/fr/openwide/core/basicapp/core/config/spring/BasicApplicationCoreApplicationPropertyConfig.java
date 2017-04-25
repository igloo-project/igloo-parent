package fr.openwide.core.basicapp.core.config.spring;

import static fr.openwide.core.basicapp.core.property.BasicApplicationCorePropertyIds.ENVIRONMENT;
import static fr.openwide.core.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS;
import static fr.openwide.core.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_VALIDATOR_ENABLED;
import static fr.openwide.core.basicapp.core.property.BasicApplicationCorePropertyIds.DATA_UPGRADE_AUTOPERFORM_TEMPLATE;

import org.springframework.context.annotation.Configuration;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.core.config.util.Environment;
import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.commons.util.functional.converter.StringCollectionConverter;
import fr.openwide.core.jpa.more.business.parameter.dao.ParameterDaoImpl;
import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyConfig;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

@Configuration
public class BasicApplicationCoreApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	protected void register(IPropertyRegistry registry) {
		registry.registerEnum(ENVIRONMENT, Environment.class, Environment.production);
		
		registry.registerBoolean(SECURITY_PASSWORD_VALIDATOR_ENABLED, true);
		registry.register(SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS, new StringCollectionConverter<>(Converter.<String>identity(), Suppliers2.<String>arrayList()), Lists.<String>newArrayList());
	
		registry.registerBoolean(DATA_UPGRADE_AUTOPERFORM_TEMPLATE, false);
	}

	@Override
	public IMutablePropertyDao mutablePropertyDao() {
		return new ParameterDaoImpl();
	}

}
