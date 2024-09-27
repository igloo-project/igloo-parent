package org.iglooproject.test.jpa.junit;

import jakarta.validation.constraints.NotNull;
import org.flywaydb.core.Flyway;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * Reinitializes a Flyway-managed test database by cleaning it before applying all migrations. The
 * re-initialization is performed before each test method is run.
 *
 * <p>This listener implies that {@link Flyway} bean is registered in spring context.
 *
 * <p>An alternative to a TestExecutionListener would be a setup method annotated with @BeforeEach
 * that invokes Flyway. But then you would lose the ability to access the database in any
 * TestExecutionListener (including the one that processes @Sql annotations) because @BeforeEach
 * runs after the TestExecutionListeners.
 */
public class SetupAndCleanDatabaseTestExecutionListener implements TestExecutionListener, Ordered {
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
