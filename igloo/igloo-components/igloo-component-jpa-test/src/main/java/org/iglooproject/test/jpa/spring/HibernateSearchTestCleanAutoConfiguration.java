package org.iglooproject.test.jpa.spring;

import com.google.common.base.Stopwatch;
import jakarta.persistence.EntityManagerFactory;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestContext;

@Configuration
@ConditionalOnClass(Search.class)
public class HibernateSearchTestCleanAutoConfiguration {

  @ConditionalOnBean(JpaProperties.class)
  public IIglooTestListener iglooHibernateSearchCleanerExecutionListener(
      JpaProperties jpaProperties, EntityManagerFactory entityManagerFactory) {
    return new IglooHibernateSearchCleanerExecutionListener(jpaProperties, entityManagerFactory);
  }

  public static class IglooHibernateSearchCleanerExecutionListener implements IIglooTestListener {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(IglooHibernateSearchCleanerExecutionListener.class);

    private final JpaProperties jpaProperties;
    private final EntityManagerFactory entityManagerFactory;

    public IglooHibernateSearchCleanerExecutionListener(
        JpaProperties jpaProperties, EntityManagerFactory entityManagerFactory) {
      this.jpaProperties = jpaProperties;
      this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void perform(IglooTestExecutionPhase phase, TestContext context) {
      if (Optional.ofNullable(jpaProperties.getProperties().get("hibernate.search.enabled"))
          .map("true"::equalsIgnoreCase)
          .orElse(false)) {
        Stopwatch sw = Stopwatch.createStarted();
        SearchMapping searchMapping = Search.mapping(entityManagerFactory);
        searchMapping.scope(Object.class).workspace().purge();
        LOGGER.info(
            "IglooTestListener: hibernate search clean performed ({} ms.)",
            sw.elapsed(TimeUnit.MILLISECONDS));
      } else {
        LOGGER.info(
            "IglooTestListener: hibernate search clean ignored as hibernate.search.enabled=false");
      }
    }
  }
}
