package test.core;

import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

//@Testcontainers
// An alternative to a TestExecutionListener would be a setup method annotated with @BeforeEach that invokes Flyway. But
// then you would lose the ability to access the database in any TestExecutionListener (incl. the one that processes
// @Sql annotations) because @BeforeEach runs after the TestExecutionListeners.
@TestExecutionListeners(
	value = {CleanDatabaseTestExecutionListener.class},
	mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS // Retains default TestExecutionListeners.
)
@Sql(scripts = "/scripts/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class AbstractBasicApplicationTestCase {
	

}
