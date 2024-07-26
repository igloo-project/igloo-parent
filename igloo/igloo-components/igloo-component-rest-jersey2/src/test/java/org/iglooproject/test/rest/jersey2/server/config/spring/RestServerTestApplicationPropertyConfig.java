package org.iglooproject.test.rest.jersey2.server.config.spring;

import org.iglooproject.spring.config.spring.AbstractApplicationPropertyConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.dao.StubMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Bean;

public class RestServerTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

  @Override
  @Bean
  public IMutablePropertyDao mutablePropertyDao() {
    return new StubMutablePropertyDao();
  }

  @Override
  public void register(IPropertyRegistry registry) {}
}
