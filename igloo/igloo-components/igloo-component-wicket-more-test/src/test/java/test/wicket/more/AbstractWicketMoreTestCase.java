package test.wicket.more;

import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.wicket.core.AbstractWicketTestCase;
import org.iglooproject.wicket.more.test.WicketMoreWicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import test.wicket.more.business.person.service.IPersonService;

public abstract class AbstractWicketMoreTestCase
    extends AbstractWicketTestCase<WicketMoreWicketTester> {

  @Autowired private IPersonService personService;

  @Autowired private WebApplication application;

  @BeforeEach
  public void setUp() throws ServiceException, SecurityServiceException {
    setWicketTester(new WicketMoreWicketTester(application));
  }

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    cleanEntities(personService);
  }
}
