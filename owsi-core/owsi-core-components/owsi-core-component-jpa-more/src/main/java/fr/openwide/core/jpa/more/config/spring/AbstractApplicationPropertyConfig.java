package fr.openwide.core.jpa.more.config.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import fr.openwide.core.jpa.more.business.property.dao.IImmutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.dao.ImmutablePropertyDaoImpl;
import fr.openwide.core.jpa.more.business.property.service.IConfigurablePropertyService;
import fr.openwide.core.jpa.more.business.property.service.PropertyServiceImpl;
import fr.openwide.core.jpa.more.config.spring.event.PropertyServiceInitEvent;

public abstract class AbstractApplicationPropertyConfig implements ApplicationListener<PropertyServiceInitEvent>  {
	
	@Autowired
	private IConfigurablePropertyService propertyService;

	@Bean
	public IImmutablePropertyDao immutablePropertyDao() {
		return new ImmutablePropertyDaoImpl();
	}

	@Bean
	public IConfigurablePropertyService propertyService() {
		return new PropertyServiceImpl();
	}

	@Override
	public void onApplicationEvent(PropertyServiceInitEvent event) {
		register(propertyService);
	}

	protected abstract void register(IConfigurablePropertyService propertyService);

}
