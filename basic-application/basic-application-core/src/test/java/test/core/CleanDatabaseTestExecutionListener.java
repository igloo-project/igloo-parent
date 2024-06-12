package test.core;

import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * Reinitialises a Flyway-managed test database by cleaning it before applying all migrations. The reinitialisation is
 * performed before each test method is run.
 */
public class CleanDatabaseTestExecutionListener implements TestExecutionListener, Ordered {
	@Override
	public void beforeTestMethod(@NotNull TestContext testContext) {
		var flyway = testContext.getApplicationContext().getBean(Flyway.class);
		flyway.clean();
		flyway.migrate();
	}

	@Override
	public int getOrder() {
		// Ensures that this TestExecutionListener is run before SqlScriptExecutionTestListener which handles @Sql.
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
