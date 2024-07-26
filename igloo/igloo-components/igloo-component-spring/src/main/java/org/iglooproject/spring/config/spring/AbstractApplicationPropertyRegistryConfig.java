package org.iglooproject.spring.config.spring;

import org.iglooproject.spring.property.service.IPropertyRegistry;

public abstract class AbstractApplicationPropertyRegistryConfig implements IPropertyRegistryConfig {
  @Override
  public abstract void register(IPropertyRegistry registry);
}
