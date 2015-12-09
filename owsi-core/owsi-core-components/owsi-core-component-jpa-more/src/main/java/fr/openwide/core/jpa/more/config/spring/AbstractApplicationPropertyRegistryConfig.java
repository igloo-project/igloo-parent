package fr.openwide.core.jpa.more.config.spring;

import org.springframework.context.ApplicationListener;

import fr.openwide.core.jpa.more.business.property.service.IPropertyRegistry;
import fr.openwide.core.jpa.more.config.spring.event.PropertyRegistryInitEvent;

public abstract class AbstractApplicationPropertyRegistryConfig implements ApplicationListener<PropertyRegistryInitEvent>  {
	
	@Override
	public void onApplicationEvent(PropertyRegistryInitEvent event) {
		register(event.getSource());
	}

	protected abstract void register(IPropertyRegistry registry);

}
