package org.iglooproject.spring.config.spring.event;

import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.ApplicationEvent;

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
