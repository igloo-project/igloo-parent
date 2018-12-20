package test.web.spring;

import org.iglooproject.jpa.util.EntityManagerUtils;
import org.iglooproject.spring.util.context.ApplicationContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class EntityManagerSeleniumExecutionListener extends AbstractTestExecutionListener {

	@Autowired
	private EntityManagerUtils entityManagerUtils;

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		refreshBean();
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		entityManagerUtils.openEntityManager();
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		entityManagerUtils.closeEntityManager();
	}

	private void refreshBean() {
		ApplicationContextUtils.getInstance().getContext().getAutowireCapableBeanFactory().autowireBean(this);
	}

}
