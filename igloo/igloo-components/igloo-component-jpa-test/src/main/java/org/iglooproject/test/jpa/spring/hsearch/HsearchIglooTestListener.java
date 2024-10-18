package org.iglooproject.test.jpa.spring.hsearch;

import com.google.common.base.Stopwatch;
import jakarta.persistence.EntityManagerFactory;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.iglooproject.test.jpa.spring.IIglooTestListener;
import org.iglooproject.test.jpa.spring.IglooTestExecutionListener;
import org.iglooproject.test.jpa.spring.IglooTestListenerEvent;
import org.iglooproject.test.jpa.spring.IglooTestListenerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;

public class HsearchIglooTestListener implements IIglooTestListener, Ordered {

  private static final Logger LOGGER = LoggerFactory.getLogger(HsearchIglooTestListener.class);

  private final String name;
  private final ApplicationEventPublisher eventPublisher;
  private final JpaProperties jpaProperties;
  private final EntityManagerFactory entityManagerFactory;

  public HsearchIglooTestListener(
      String name,
      JpaProperties jpaProperties,
      EntityManagerFactory entityManagerFactory,
      ApplicationEventPublisher applicationEventPublisher) {
    this.name = name;
    this.eventPublisher = applicationEventPublisher;
    this.jpaProperties = jpaProperties;
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void before(IglooTestListenerType type, TestContext context) {
    if (Optional.ofNullable(jpaProperties.getProperties().get("hibernate.search.enabled"))
        .map("true"::equalsIgnoreCase)
        .orElse(false)) {
      Stopwatch sw = Stopwatch.createStarted();
      HsearchUtil.cleanIndexes(entityManagerFactory);
      eventPublisher.publishEvent(new IglooTestListenerEvent("hibernate-search:clean"));
      LOGGER.info(
          "IglooTestListener: hibernate search clean performed ({} ms.)",
          sw.elapsed(TimeUnit.MILLISECONDS));
    } else {
      LOGGER.info(
          "IglooTestListener: hibernate search clean ignored as hibernate.search.enabled=false");
    }
  }

  @Override
  public boolean match(String name) {
    return this.name.equals(name);
  }

  @Override
  public int getOrder() {
    return IglooTestExecutionListener.ORDER_CACHE;
  }
}
