package fr.openwide.core.test.wicket.more;

import org.apache.wicket.util.tester.WicketTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.junit.AbstractTestCase;
import fr.openwide.core.test.wicket.more.business.person.service.IPersonService;
import fr.openwide.core.test.wicket.more.config.spring.WicketMoreTestWebappConfig;
import fr.openwide.core.test.wicket.more.junit.IWicketTestCase;
import fr.openwide.core.test.wicket.more.junit.WicketTesterTestExecutionListener;

@ContextConfiguration(classes = WicketMoreTestWebappConfig.class)
@TestExecutionListeners({ WicketTesterTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@DirtiesContext
public abstract class AbstractWicketMoreTestCase extends AbstractTestCase implements IWicketTestCase {
	
	private WicketTester wicketTester;
	
	@Autowired
	private IPersonService personService;

	@Override
	public void setWicketTester(WicketTester wicketTester) {
		this.wicketTester = wicketTester;
	}

	public WicketTester getWicketTester() {
		return wicketTester;
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(personService);
	}

}