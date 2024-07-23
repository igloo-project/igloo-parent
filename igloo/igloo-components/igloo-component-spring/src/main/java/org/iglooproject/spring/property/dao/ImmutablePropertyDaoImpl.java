package org.iglooproject.spring.property.dao;

import org.iglooproject.spring.config.CorePropertyPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Autowired;

public class ImmutablePropertyDaoImpl implements IImmutablePropertyDao {

  @Autowired private CorePropertyPlaceholderConfigurer configurer;

  @Override
  public String get(String key) {
    return configurer.getProperty(key);
  }
}
