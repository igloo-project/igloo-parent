package fr.openwide.core.jpa.junit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import fr.openwide.core.jpa.util.EntityManagerUtils;


public class EntityManagerExecutionListener extends AbstractTestExecutionListener implements TestExecutionListener {

	@Autowired
	private EntityManagerUtils entityManagerUtils;

	public void beforeTestClass(TestContext testContext) throws Exception {
		refreshBean(testContext);
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
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
