package org.iglooproject.test.jpa.spring.hsearch;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.test.jpa.spring.IIglooTestListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Search.class)
public class HsearchIglooTestListenerAutoConfiguration {

  @Bean
  @ConditionalOnBean(JpaProperties.class)
  public IIglooTestListener hsearchIglooTestListener(
      JpaProperties jpaProperties,
      EntityManagerFactory entityManagerFactory,
      ApplicationEventPublisher eventPublisher) {
    return new HsearchIglooTestListener(
        "default", jpaProperties, entityManagerFactory, eventPublisher);
  }
}
