package fr.openwide.core.test.wicket.more;

import org.apache.wicket.util.tester.WicketTester;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.wicket.more.config.spring.WicketMoreTestWebappConfig;
import fr.openwide.core.test.wicket.more.junit.IWicketTestCase;
import fr.openwide.core.test.wicket.more.junit.WicketTesterTestExecutionListener;

@ContextConfiguration(classes = WicketMoreTestWebappConfig.class)
@TestExecutionListeners({ WicketTesterTestExecutionListener.class })
public abstract class AbstractWicketMoreJpaTestCase extends AbstractJpaCoreTestCase implements IWicketTestCase {
	
	private WicketTester wicketTester;

	@Override
	public void setWicketTester(WicketTester wicketTester) {
		this.wicketTester = wicketTester;
	}

	public WicketTester getWicketTester() {
		return wicketTester;
	}

}