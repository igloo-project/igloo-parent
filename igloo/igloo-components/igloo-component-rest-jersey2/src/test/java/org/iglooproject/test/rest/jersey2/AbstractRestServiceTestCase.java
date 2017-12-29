package org.iglooproject.test.rest.jersey2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.junit.AbstractTestCase;
import org.iglooproject.test.rest.jersey2.business.person.service.IPersonService;
import org.iglooproject.test.rest.jersey2.client.config.spring.RestClientTestCoreCommonConfig;

@ContextConfiguration(classes = RestClientTestCoreCommonConfig.class)
public abstract class AbstractRestServiceTestCase extends AbstractTestCase {
	
	@Autowired
	private IPersonService personService;

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(personService);
	}

}