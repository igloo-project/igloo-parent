package test.wicket.more;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.wicket.more.CoreWicketTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import test.wicket.more.business.person.service.IPersonService;
import test.wicket.more.config.spring.SimpleWicketMoreTestWebappConfig;

@ContextConfiguration(classes = SimpleWicketMoreTestWebappConfig.class)
public class AbstractWicketMoreTestCase extends org.iglooproject.test.wicket.more.AbstractWicketMoreTestCase<CoreWicketTester> {
	
	@Autowired
	private IPersonService personService;

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(personService);
	}

}
