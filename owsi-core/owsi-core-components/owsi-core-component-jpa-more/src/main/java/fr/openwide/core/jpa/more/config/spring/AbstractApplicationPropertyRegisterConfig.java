package fr.openwide.core.jpa.more.config.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import fr.openwide.core.jpa.more.business.property.service.IConfigurablePropertyService;
import fr.openwide.core.jpa.more.config.spring.event.PropertyServiceInitEvent;

public abstract class AbstractApplicationPropertyRegisterConfig implements ApplicationListener<PropertyServiceInitEvent>  {
	
	@Autowired
	private IConfigurablePropertyService propertyService;

	@Override
	public void onApplicationEvent(PropertyServiceInitEvent event) {
		register(propertyService);
	}

	protected abstract void register(IConfigurablePropertyService propertyService);

}
