package fr.openwide.core.test.wicket.more.junit;

import org.apache.wicket.Application;
import org.apache.wicket.util.tester.WicketTester;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import fr.openwide.core.wicket.more.application.CoreWicketApplication;

/**
 * Hack pour permettre de conserver le même WicketTester pour toutes les méthodes du Test.
 * <p>Impossible de passer par une propriété ou une méthode static dans le test lui-même, vu que l'application est obtenue via le contexte spring. 
 * <p>Il est nécessaire d'utiliser à chaque fois le même WicketTester parce qu'on ne peut pas utiliser plusieurs WicketTester sur la même {@link Application},
 * et je (YRO) n'ai pas réussi à renouveler l'application entre chaque méthode avec le DirtiesContextTestExecutionListener.
 */
public class WicketTesterTestExecutionListener extends AbstractTestExecutionListener {
	
	private static String WICKET_TESTER_ATTRIBUTE = WicketTesterTestExecutionListener.class.getName() + ".WICKET_TESTER";

	// On réutilise la même application pour chaque test (beforeTestClass, et pas beforeTestMethod)
	// C'est un peu sale, mais autrement, il faudrait réinitialiser le contexte spring (dont l'application wicket fait partie),
	// et 
	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		CoreWicketApplication application = testContext.getApplicationContext().getAutowireCapableBeanFactory().getBean(CoreWicketApplication.class);
		WicketTester tester = new WicketTester(application);
		testContext.setAttribute(WICKET_TESTER_ATTRIBUTE, tester);
	}
	
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		Object testInstance = testContext.getTestInstance();
		if (! (testInstance instanceof IWicketTestCase)) {
			throw new IllegalStateException("This execution listener can only be used on an " + IWicketTestCase.class.getSimpleName());
		}
		IWicketTestCase testCase = (IWicketTestCase) testContext.getTestInstance();
		WicketTester tester = (WicketTester) testContext.getAttribute(WICKET_TESTER_ATTRIBUTE);
		if (!(tester instanceof WicketTester)) {
			throw new IllegalStateException("Missing or invalid wicket tester - someone messed up with the test context.");
		}
		testCase.setWicketTester((WicketTester)tester);
	}
	
	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		WicketTester tester = (WicketTester) testContext.getAttribute(WICKET_TESTER_ATTRIBUTE);
		if (!(tester instanceof WicketTester)) {
			throw new IllegalStateException("Missing or invalid wicket tester - someone messed up with the test context.");
		}
		tester.destroy();
	}
}
