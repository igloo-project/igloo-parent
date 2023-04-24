package org.iglooproject.test.rest.jersey2;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.iglooproject.test.rest.jersey2.business.person.service.IPersonService;
import org.iglooproject.test.rest.jersey2.server.MockServlet;
import org.iglooproject.test.web.context.MockRestServer;
import org.iglooproject.test.web.context.MockServletTestExecutionListener;
import org.iglooproject.test.webjpa.context.AbstractMockJpaRestServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

@TestExecutionListeners(listeners = MockServletTestExecutionListener.class)
@ContextConfiguration(classes = {})
@MockRestServer
public abstract class AbstractRestServiceTestCase extends AbstractTestCase {
	
	@Autowired
	private IPersonService personService;
	
	@Autowired
	private MockServlet serverResource;

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(personService);
	}

	public AbstractMockJpaRestServlet getServerResource() {
		return serverResource;
	}

}