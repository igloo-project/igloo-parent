package fr.openwide.core.spring.config.spring;

import org.springframework.context.annotation.Bean;

import fr.openwide.core.spring.property.dao.IImmutablePropertyDao;
import fr.openwide.core.spring.property.dao.ImmutablePropertyDaoImpl;
import fr.openwide.core.spring.property.service.IConfigurablePropertyService;
import fr.openwide.core.spring.property.service.PropertyServiceImpl;

public abstract class AbstractApplicationPropertyConfig extends AbstractApplicationPropertyRegistryConfig {

	@Bean
	public IImmutablePropertyDao immutablePropertyDao() {
		return new ImmutablePropertyDaoImpl();
	}

	@Bean
	public IConfigurablePropertyService propertyService() {
		return new PropertyServiceImpl();
	}

}
