package igloo.test.listener.postgresql;

import igloo.test.listener.IIglooTestListener;
import igloo.test.listener.IglooTestExecutionListener;
import igloo.test.listener.model.IglooTestListenerEvent;
import igloo.test.listener.model.IglooTestListenerType;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.autoconfigure.JdbcConnectionDetails;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Perform database clean before each test by truncating tables. Clean is skipped with an WARN log
 * if database is not postgresql (based on {@code driver == org.postgresql.Driver}.
 *
 * <p>Clean behavior can be controlled by {@link PsqlCleanerProperties}.
 *
 * @see PsqlIglooTestListenerAutoConfiguration
 */
public class PsqlIglooTestListener implements IIglooTestListener, Ordered {
  private static final Logger LOGGER = LoggerFactory.getLogger(PsqlIglooTestListener.class);

  private final String name;
  private final ApplicationEventPublisher eventPublisher;
  private final JdbcConnectionDetails jdbcConnectionDetails;
  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate transactionTemplate;
  private final PsqlCleanerProperties databaseCleanerProperties;

  public PsqlIglooTestListener(
      String name,
      ApplicationEventPublisher eventPublisher,
      JdbcConnectionDetails jdbcConnectionDetails,
      PlatformTransactionManager platformTransactionManager,
      DataSource dataSource,
      PsqlCleanerProperties databaseCleanerProperties) {
    this.name = name;
    this.eventPublisher = eventPublisher;
    this.jdbcConnectionDetails = jdbcConnectionDetails;
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
    this.databaseCleanerProperties = databaseCleanerProperties;
  }

  @Override
  public void before(IglooTestListenerType type, TestContext context) {
    if (!"org.postgresql.Driver".equals(jdbcConnectionDetails.getDriverClassName())) {
      LOGGER.warn(
          "Psql - skipped as datasource is not a postgresql database ({})",
          jdbcConnectionDetails.getDriverClassName());
      return;
    }
    eventPublisher.publishEvent(new IglooTestListenerEvent("psql:clean"));
    PsqlUtil.cleanDatabase(transactionTemplate, jdbcTemplate, databaseCleanerProperties);
  }

  @Override
  public int getOrder() {
    return IglooTestExecutionListener.ORDER_DATABASE;
  }

  @Override
  public boolean match(String name) {
    return this.name.equals(name);
  }
}
