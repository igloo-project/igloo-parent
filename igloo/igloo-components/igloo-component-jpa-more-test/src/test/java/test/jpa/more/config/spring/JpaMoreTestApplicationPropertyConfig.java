package test.jpa.more.config.spring;

import org.iglooproject.jpa.more.business.parameter.dao.ParameterDaoImpl;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaMoreTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

  @Override
  public void register(IPropertyRegistry registry) {}

  @Override
  public IMutablePropertyDao mutablePropertyDao() {
    return new ParameterDaoImpl();
  }
}
