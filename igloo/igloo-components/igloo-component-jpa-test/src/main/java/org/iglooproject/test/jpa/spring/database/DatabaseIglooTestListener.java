package org.iglooproject.test.jpa.spring.database;

import javax.sql.DataSource;
import org.iglooproject.test.jpa.spring.IIglooTestListener;
import org.iglooproject.test.jpa.spring.IglooTestExecutionListener;
import org.iglooproject.test.jpa.spring.IglooTestListenerType;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class DatabaseIglooTestListener implements IIglooTestListener, Ordered {

  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate transactionTemplate;
  private final DatabaseCleanerProperties databaseCleanerProperties;

  public DatabaseIglooTestListener(
      PlatformTransactionManager platformTransactionManager,
      DataSource dataSource,
      DatabaseCleanerProperties databaseCleanerProperties) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
    this.databaseCleanerProperties = databaseCleanerProperties;
  }

  @Override
  public void before(IglooTestListenerType type, TestContext context) {
    DatabaseUtil.cleanDatabase(transactionTemplate, jdbcTemplate, databaseCleanerProperties);
  }

  @Override
  public int getOrder() {
    return IglooTestExecutionListener.ORDER_DATABASE;
  }
}
