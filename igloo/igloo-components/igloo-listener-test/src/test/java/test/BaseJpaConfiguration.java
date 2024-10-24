package test;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import igloo.test.listener.cache.Level2CacheIglooTestListenerAutoConfiguration;
import igloo.test.listener.hsearch.HsearchIglooTestListenerAutoConfiguration;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@ImportAutoConfiguration(
    classes = {
      DataSourceAutoConfiguration.class,
      HibernateJpaAutoConfiguration.class,
      HsearchIglooTestListenerAutoConfiguration.class,
      Level2CacheIglooTestListenerAutoConfiguration.class,
      EventRecorderConfiguration.class
    })
@EntityScan(basePackageClasses = TestIglooTestExecutionListener.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface BaseJpaConfiguration {}
