package fr.openwide.test.core.rest.jersey2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.junit.AbstractTestCase;
import fr.openwide.test.core.rest.jersey2.business.person.service.IPersonService;
import fr.openwide.test.core.rest.jersey2.client.config.spring.RestClientTestCoreCommonConfig;

@ContextConfiguration(classes = RestClientTestCoreCommonConfig.class)
public abstract class AbstractRestServiceTestCase extends AbstractTestCase {
	
	@Autowired
	private IPersonService personService;

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(personService);
	}

}