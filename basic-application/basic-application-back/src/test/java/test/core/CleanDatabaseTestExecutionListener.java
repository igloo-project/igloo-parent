package test.core;

import jakarta.validation.constraints.NotNull;
import org.flywaydb.core.Flyway;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * Reinitialises a Flyway-managed test database by cleaning it before applying all migrations. The
 * reinitialisation is performed before each test method is run.
 *
 * <p>An alternative to a TestExecutionListener would be a setup method annotated with @BeforeEach
 * that invokes Flyway. But then you would lose the ability to access the database in any
 * TestExecutionListener (incl. the one that processes @Sql annotations) because @BeforeEach runs
 * after the TestExecutionListeners.
 */
public class CleanDatabaseTestExecutionListener implements TestExecutionListener, Ordered {
  @Override
  public void beforeTestMethod(@NotNull TestContext testContext) {
    Flyway flyway = testContext.getApplicationContext().getBean(Flyway.class);
    flyway.clean();
    flyway.migrate();
  }

  @Override
  public int getOrder() {
    // Ensures that this TestExecutionListener is run before SqlScriptsTestExecutionListener which
    // handles @Sql.
    return 5000 - 1000; // 5000 = SqlScriptsTestExecutionListener#order
  }
}
