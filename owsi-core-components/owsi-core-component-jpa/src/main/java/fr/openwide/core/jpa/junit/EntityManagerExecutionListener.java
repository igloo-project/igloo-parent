package fr.openwide.core.jpa.junit;
import javax.persistence.PersistenceContext;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import fr.openwide.core.jpa.util.EntityManagerUtils;


public class EntityManagerExecutionListener extends AbstractTestExecutionListener implements TestExecutionListener {

	@PersistenceContext
	private EntityManagerUtils entityManagerUtils;

	public void beforeTestClass(TestContext testContext) throws Exception {
		refreshBean(testContext);
		entityManagerUtils.openEntityManager();
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		entityManagerUtils.closeEntityManager();
	}

	private void refreshBean(TestContext testContext) {
		testContext.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
	}

}
