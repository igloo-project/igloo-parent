package org.iglooproject.spring.config.spring;

import org.springframework.context.ApplicationListener;

import org.iglooproject.spring.config.spring.event.PropertyRegistryInitEvent;
import org.iglooproject.spring.property.service.IPropertyRegistry;

public abstract class AbstractApplicationPropertyRegistryConfig implements ApplicationListener<PropertyRegistryInitEvent>  {
	
	@Override
	public void onApplicationEvent(PropertyRegistryInitEvent event) {
		register(event.getSource());
	}

	protected abstract void register(IPropertyRegistry registry);

}
