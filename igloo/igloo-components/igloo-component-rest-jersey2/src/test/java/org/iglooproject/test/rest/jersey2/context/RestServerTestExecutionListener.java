package org.iglooproject.test.rest.jersey2.context;

import org.springframework.context.ApplicationContext;
import org.springframework.core.Conventions;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class RestServerTestExecutionListener extends AbstractTestExecutionListener {

	public static final String ACTIVATE_LISTENER = Conventions.getQualifiedAttributeName(
			RestServerTestExecutionListener.class, "activateListener");
	public static final String POPULATED_REQUEST_CONTEXT_HOLDER_ATTRIBUTE = Conventions.getQualifiedAttributeName(
			RestServerTestExecutionListener.class, "populatedRequestContextHolder");
	public static final String SERVER_ATTRIBUTE = Conventions.getQualifiedAttributeName(
			RestServerTestExecutionListener.class, "server");

	public boolean isActivated(TestContext testContext) {
		return (Boolean.TRUE.equals(testContext.getAttribute(ACTIVATE_LISTENER)) ||
				AnnotatedElementUtils.hasAnnotation(testContext.getTestClass(), MockRestServer.class));
	}

	private boolean alreadyPopulatedRequestContextHolder(TestContext testContext) {
		return Boolean.TRUE.equals(testContext.getAttribute(POPULATED_REQUEST_CONTEXT_HOLDER_ATTRIBUTE));
	}

	@Override
	public void prepareTestInstance(TestContext testContext) {
		if (!isActivated(testContext) || alreadyPopulatedRequestContextHolder(testContext)) {
			return;
		}
		
		ApplicationContext applicationContext = testContext.getApplicationContext();
		
		RestServerTestResource server = applicationContext.getBean(RestServerTestResource.class);
		testContext.setAttribute(SERVER_ATTRIBUTE, server);
	}
	
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		if (testContext.hasAttribute(SERVER_ATTRIBUTE)) {
			((RestServerTestResource) testContext.getAttribute(SERVER_ATTRIBUTE)).prepare();
		}
	}
	
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		if (testContext.hasAttribute(SERVER_ATTRIBUTE)) {
			((RestServerTestResource) testContext.getAttribute(SERVER_ATTRIBUTE)).tearDown();
		}
	}

}
