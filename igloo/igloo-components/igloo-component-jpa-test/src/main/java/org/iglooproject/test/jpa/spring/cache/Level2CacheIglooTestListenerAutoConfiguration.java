package org.iglooproject.test.jpa.spring.cache;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Cache;
import org.iglooproject.test.jpa.spring.IIglooTestListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Cache.class)
public class Level2CacheIglooTestListenerAutoConfiguration {

  @Bean
  @ConditionalOnBean(JpaProperties.class)
  public IIglooTestListener level2IglooTestListener(
      JpaProperties jpaProperties,
      EntityManagerFactory entityManagerFactory,
      ApplicationEventPublisher eventPublisher) {
    return new Level2CacheIglooTestListener(
        "default", jpaProperties, entityManagerFactory, eventPublisher);
  }
}
