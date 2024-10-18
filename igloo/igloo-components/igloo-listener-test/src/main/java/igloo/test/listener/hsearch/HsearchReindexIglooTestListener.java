package igloo.test.listener.hsearch;

import com.google.common.base.Stopwatch;
import igloo.test.listener.IIglooTestListener;
import igloo.test.listener.IglooTestExecutionListener;
import igloo.test.listener.model.IglooTestListenerEvent;
import igloo.test.listener.model.IglooTestListenerType;
import jakarta.persistence.EntityManagerFactory;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;

/**
 * Perform a hibernate search reindexation. This {@link IIglooTestListener} must be installed in a
 * {@link TestExecutionListener} installed after {@link SqlScriptsTestExecutionListener}.
 *
 * @see HsearchIglooTestListenerAutoConfiguration
 */
public class HsearchReindexIglooTestListener implements IIglooTestListener, Ordered {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(HsearchReindexIglooTestListener.class);

  private final String name;
  private final ApplicationEventPublisher eventPublisher;
  private final JpaProperties jpaProperties;
  private final EntityManagerFactory entityManagerFactory;

  public HsearchReindexIglooTestListener(
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
    // if empty and HibernateSearchIntegrator is on the classpath, hibernate search is enabled
    if (Optional.ofNullable(jpaProperties.getProperties().get("hibernate.search.enabled"))
        .map("true"::equalsIgnoreCase)
        .orElse(true)) {
      Stopwatch sw = Stopwatch.createStarted();
      HsearchUtil.reindexAll(entityManagerFactory);
      eventPublisher.publishEvent(new IglooTestListenerEvent("hibernate-search:reindex"));
      LOGGER.info(
          "IglooTestListener: hibernate search reindex performed ({} ms.)",
          sw.elapsed(TimeUnit.MILLISECONDS));
    } else {
      LOGGER.info(
          "IglooTestListener: hibernate search reindex ignored as hibernate.search.enabled=false");
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
