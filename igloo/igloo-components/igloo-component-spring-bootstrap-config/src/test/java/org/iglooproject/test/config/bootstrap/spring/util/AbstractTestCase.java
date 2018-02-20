package org.iglooproject.test.config.bootstrap.spring.util;

import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class
})
public abstract class AbstractTestCase {

	/**
	 * Use Spring injection
	 */
	@ClassRule
	public static final SpringClassRule SCR = new SpringClassRule();
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	protected ConfigurableApplicationContext applicationContext;

	@Autowired
	protected ConfigurableEnvironment environment;

}
