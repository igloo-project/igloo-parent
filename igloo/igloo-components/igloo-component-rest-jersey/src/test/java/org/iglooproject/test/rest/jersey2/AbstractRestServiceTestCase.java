package org.iglooproject.test.rest.jersey2;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.iglooproject.test.rest.jersey2.business.person.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRestServiceTestCase extends AbstractTestCase {

  @Autowired private IPersonService personService;

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    cleanEntities(personService);
  }
}
