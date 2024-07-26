package org.iglooproject.spring.config.spring;

import org.iglooproject.spring.property.dao.IImmutablePropertyDao;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.dao.ImmutablePropertyDaoImpl;
import org.iglooproject.spring.property.service.IConfigurablePropertyService;
import org.iglooproject.spring.property.service.PropertyServiceImpl;
import org.springframework.context.annotation.Bean;

public abstract class AbstractApplicationPropertyConfig
    extends AbstractApplicationPropertyRegistryConfig {

  @Bean
  public abstract IMutablePropertyDao mutablePropertyDao();

  @Bean
  public IImmutablePropertyDao immutablePropertyDao() {
    return new ImmutablePropertyDaoImpl();
  }

  @Bean
  public IConfigurablePropertyService propertyService() {
    return new PropertyServiceImpl();
  }
}
