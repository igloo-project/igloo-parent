package org.iglooproject.test.config.bootstrap.spring;

import org.assertj.core.api.Assertions;
import org.iglooproject.config.bootstrap.spring.ExtendedTestApplicationContextInitializer;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class
})
@ContextConfiguration(initializers = ExtendedTestApplicationContextInitializer.class)
@TestPropertySource(properties = {
	"igloo.bootstrapLocations=classpath:test-bootstrap.properties,classpath:test-bootstrap-override.properties",
	"testPropertySourceNotOverridenProperty=@TestPropertySource",
	"testPropertySourceAndBootstrapProperty1=@TestPropertySource",
	"testPropertySourceAndBootstrapProperty2=@TestPropertySource",
	"igloo.bootstrapOverrideDefault=true"
})
@Import(SpringConfig.class)
public class SpringBoostrapTest {

	/**
	 * Use Spring injection
	 */
	@ClassRule
	public static final SpringClassRule SCR = new SpringClassRule();
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Test override precedence. TestPropertySource -&gt; bootstrap file 1 -&gt; bootstrap file 2
	 */
	@Test
	public void testOverrides() {
		// not overriden
		Assertions.assertThat(applicationContext.getEnvironment().getProperty("testPropertySourceNotOverridenProperty")).isEqualTo("@TestPropertySource");
		// @TestPropertySource overrides all
		Assertions.assertThat(applicationContext.getEnvironment().getProperty("testPropertySourceAndBootstrapProperty1")).isEqualTo("@TestPropertySource");
		Assertions.assertThat(applicationContext.getEnvironment().getProperty("testPropertySourceAndBootstrapProperty2")).isEqualTo("@TestPropertySource");
		// defined in first bootstrap file
		Assertions.assertThat(applicationContext.getEnvironment().getProperty("bootstrapOverridenProperty1")).isEqualTo("test-bootstrap.properties");
		// defined in first bootstrap file and overriden in second bootstrap file
		Assertions.assertThat(applicationContext.getEnvironment().getProperty("bootstrapOverridenProperty2")).isEqualTo("test-bootstrap-override.properties");
	}

}
