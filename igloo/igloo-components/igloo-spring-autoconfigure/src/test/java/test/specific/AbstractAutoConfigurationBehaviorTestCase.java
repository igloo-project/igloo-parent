package test.specific;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers only Igloo auto-configuration,
 * without Spring Boot auto-configuration. Subclasses test with a custom {@link ContextConfiguration}:
 * 
 * <ul>
 * 	<li>Spring Boot alone behavior, as a reference behavior (test for Java MailSender bean)</li>
 *  <li>Igloo auto-configuration behavior</li>
 *  <li>Both configuration behavior</li>
 * </ul>
 * 
 * This class enables Spring injection and add spring.mail.host property so that spring boot auto-configuration can be
 * triggered.
 * 
 * This test relies on the fact that javax.mail and spring-mail is available on classpath.
 * 
 * Each subclass must provide a custom {@link ContextConfiguration} and test its own behavior.
 */
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
})
@TestPropertySource(properties = "spring.mail.host=localhost")
public abstract class AbstractAutoConfigurationBehaviorTestCase {

	@ClassRule
	public static final SpringClassRule SCR = new SpringClassRule();
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	protected ApplicationContext applicationContext;

}
