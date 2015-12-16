package fr.openwide.core.spring.config.spring;

import org.springframework.context.ApplicationListener;

import fr.openwide.core.spring.config.spring.event.PropertyRegistryInitEvent;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

public abstract class AbstractApplicationPropertyRegistryConfig implements ApplicationListener<PropertyRegistryInitEvent>  {
	
	@Override
	public void onApplicationEvent(PropertyRegistryInitEvent event) {
		register(event.getSource());
	}

	protected abstract void register(IPropertyRegistry registry);

}
