package org.iglooproject.test.listener;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.iglooproject.test.jpa.spring.cache.Level2CacheIglooTestListenerAutoConfiguration;
import org.iglooproject.test.jpa.spring.hsearch.HsearchIglooTestListenerAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
    classes = {
      DataSourceAutoConfiguration.class,
      HibernateJpaAutoConfiguration.class,
      HsearchIglooTestListenerAutoConfiguration.class,
      Level2CacheIglooTestListenerAutoConfiguration.class,
      EventRecorderConfiguration.class
    })
@EntityScan(basePackageClasses = TestIglooExecutionListener.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface BaseJpaConfiguration {}
