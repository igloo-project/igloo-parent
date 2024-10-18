package igloo.test.listener.hsearch;

import igloo.test.listener.IIglooTestListener;
import igloo.test.listener.IglooTestExecutionListener;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.search.mapper.orm.Search;
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
 * Autoconfiguration enabling a clear of hibernate search indexes before each test. Can be disabled
 * by {@code igloo.test.listener.hsearch.enabled=false}. Enabled by default.
 *
 * <p>If hibernate search API are not available, bean is skipped. If API are available but hibernate
 * search is not configured, cache clearing is skipped.
 *
 * <p>{@link IglooTestExecutionListener} must be a registered {@link TestExecutionListener} to
 * invoke this {@link IIglooTestListener}.
 */
@AutoConfiguration(after = HibernateJpaAutoConfiguration.class)
@ConditionalOnProperty(
    prefix = "igloo.test.listener.hsearch",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
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

  @Bean
  @ConditionalOnBean(JpaProperties.class)
  public IIglooTestListener hsearchReindexIglooTestListener(
      JpaProperties jpaProperties,
      EntityManagerFactory entityManagerFactory,
      ApplicationEventPublisher eventPublisher) {
    return new HsearchReindexIglooTestListener(
        "after-sql", jpaProperties, entityManagerFactory, eventPublisher);
  }
}
