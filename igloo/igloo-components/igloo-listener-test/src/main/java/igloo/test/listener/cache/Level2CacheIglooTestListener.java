package igloo.test.listener.cache;

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
 * Perform a level 2 cache clean before each test. Clean is skipped with an INFO log if second level
 * cache is not enabled (based on {@ hibernate.cache.use_second_level_cache} from {@link
 * JpaProperties}).
 *
 * @see Level2CacheIglooTestListenerAutoConfiguration
 */
public class Level2CacheIglooTestListener implements IIglooTestListener, Ordered {

  private static final Logger LOGGER = LoggerFactory.getLogger(Level2CacheIglooTestListener.class);

  private final String name;
  private final ApplicationEventPublisher eventPublisher;
  private final JpaProperties jpaProperties;
  private final EntityManagerFactory entityManagerFactory;

  public Level2CacheIglooTestListener(
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
    if (Optional.ofNullable(
            jpaProperties.getProperties().get("hibernate.cache.use_second_level_cache"))
        .map("true"::equalsIgnoreCase)
        .orElse(false)) {
      Stopwatch sw = Stopwatch.createStarted();
      entityManagerFactory.getCache().evictAll();
      eventPublisher.publishEvent(new IglooTestListenerEvent("hibernate-cache:clean"));
      LOGGER.info(
          "IglooTestListener: hibernate cache clean performed ({} ms.)",
          sw.elapsed(TimeUnit.MILLISECONDS));
    } else {
      LOGGER.info(
          "IglooTestListener: hibernate cache clean ignored as hibernate.cache.use_second_level_cache=false");
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
