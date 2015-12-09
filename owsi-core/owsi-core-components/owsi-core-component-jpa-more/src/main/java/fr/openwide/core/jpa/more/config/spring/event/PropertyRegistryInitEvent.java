package fr.openwide.core.jpa.more.config.spring.event;

import org.springframework.context.ApplicationEvent;

import fr.openwide.core.jpa.more.business.property.service.IPropertyRegistry;

public class PropertyRegistryInitEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1735401082005771273L;

	public PropertyRegistryInitEvent(IPropertyRegistry propertyService) {
		super(propertyService);
	}
	
	@Override
	public IPropertyRegistry getSource() {
		return (IPropertyRegistry) source;
	}

}
