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
import org.springframework.boot.jpa.autoconfigure.JpaProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;

/**
 * Perform a hibernate search indexes clean before each test. Clean is skipped with an INFO log if
 * hibernate-search is not enabled (based on {@ hibernate.search.enabled} from {@link
 * JpaProperties}).
 *
 * @see HsearchIglooTestListenerAutoConfiguration
 */
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
    // if empty and HibernateSearchIntegrator is on the classpath, hibernate search is enabled
    if (Optional.ofNullable(jpaProperties.getProperties().get("hibernate.search.enabled"))
        .map("true"::equalsIgnoreCase)
        .orElse(true)) {
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
