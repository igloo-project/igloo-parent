package test.core;

import org.hibernate.internal.SessionImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

//@Testcontainers
// An alternative to a TestExecutionListener would be a setup method annotated with @BeforeEach that invokes Flyway. But
// then you would lose the ability to access the database in any TestExecutionListener (incl. the one that processes
// @Sql annotations) because @BeforeEach runs after the TestExecutionListeners.
@TestExecutionListeners(
	value = {CleanDatabaseTestExecutionListener.class},
	mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS // Retains default TestExecutionListeners.
)
@Sql(scripts = "/scripts/init.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class AbstractBasicApplicationTestCase extends AbstractTestCase {

	@BeforeEach
	@Override
	public void init() {
		//clear cache
		((SessionImpl) getEntityManager().getDelegate()).getSessionFactory().getCache().evictAllRegions();
	}

	@AfterEach
	@Override
	public void close() throws ServiceException, SecurityServiceException {
		// nothing to do
	}

	@Override
	protected void cleanAll()  {
		// nothing to do
	}
}
