package org.iglooproject.jpa.more.autoconfigure;

import org.iglooproject.jpa.more.business.parameter.dao.ParameterDaoImpl;
import org.iglooproject.spring.autoconfigure.PropertyIdsAutoConfiguration;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IConfigurablePropertyService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@AutoConfiguration(after = PropertyIdsAutoConfiguration.class)
@Configuration
@Import(JpaMoreComponentScanConfiguration.class)
public class JpaMoreAutoConfiguration {

  public JpaMoreAutoConfiguration() { // NOSONAR
    // nothing
  }

  @ConditionalOnBean(IConfigurablePropertyService.class)
  @Configuration
  public static class JpaMutablePropertyService {
    @Bean
    public IMutablePropertyDao mutablePropertyDao() {
      return new ParameterDaoImpl();
    }
  }
}
