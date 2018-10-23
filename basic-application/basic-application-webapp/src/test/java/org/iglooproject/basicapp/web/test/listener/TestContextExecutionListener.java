package org.iglooproject.basicapp.web.test.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class TestContextExecutionListener extends AbstractTestExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestContextExecutionListener.class);

	private static TestContext TEST_CONTEXT;

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		setStaticContext(testContext);
	}

	private static void setStaticContext(TestContext testContext) {
		if (TEST_CONTEXT != null && !TEST_CONTEXT.equals(testContext)) {
			LOGGER.warn("A different Spring context has already been registered for testing.");
		}
		TEST_CONTEXT = testContext;
	}

	public static TestContext getTestContext() {
		return TEST_CONTEXT;
	}
}
