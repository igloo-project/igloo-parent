package org.iglooproject.spring.autoconfigure;

import org.iglooproject.spring.property.dao.IImmutablePropertyDao;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.dao.ImmutablePropertyDaoImpl;
import org.iglooproject.spring.property.service.FallbackMutablePropertyDaoImpl;
import org.iglooproject.spring.property.service.IConfigurablePropertyService;
import org.iglooproject.spring.property.service.PropertyServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyIdsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public IMutablePropertyDao fallbackMutablePropertyDao(IImmutablePropertyDao immutablePropertyDao) {
		return new FallbackMutablePropertyDaoImpl(immutablePropertyDao);
	}

	@Bean
	public IImmutablePropertyDao immutablePropertyDao() {
		return new ImmutablePropertyDaoImpl();
	}

	@Bean
	public IConfigurablePropertyService propertyService() {
		return new PropertyServiceImpl();
	}

}
