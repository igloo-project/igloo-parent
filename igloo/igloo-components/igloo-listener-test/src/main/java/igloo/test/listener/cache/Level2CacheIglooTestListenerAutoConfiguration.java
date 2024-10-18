package igloo.test.listener.cache;

import igloo.test.listener.IIglooTestListener;
import igloo.test.listener.IglooTestExecutionListener;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Cache;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestExecutionListener;

/**
 * Autoconfiguration enabling a clear of second level cache before each test. Can be disabled by
 * {@code igloo.test.listener.cache-level2.enabled=false}. Enabled by default.
 *
 * <p>If cache API are not available, bean is skipped. If API are available but second level cache
 * is not configured, cache clearing is skipped.
 *
 * <p>{@link IglooTestExecutionListener} must be a registered {@link TestExecutionListener} to
 * invoke this {@link IIglooTestListener}.
 */
@AutoConfiguration(after = HibernateJpaAutoConfiguration.class)
@ConditionalOnProperty(
    prefix = "igloo.test.listener.cache-level2",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
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
