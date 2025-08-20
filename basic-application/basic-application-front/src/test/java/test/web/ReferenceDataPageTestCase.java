package test.web;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static org.junit.jupiter.api.Assertions.assertThrows;

import basicapp.front.referencedata.page.ReferenceDataPage;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.Test;
import test.web.config.BasicApplicationFrontSpringBootTest;

@BasicApplicationFrontSpringBootTest
class ReferenceDataPageTestCase extends AbstractBasicApplicationFrontTestCase {

  @Test
  void initPage() throws ServiceException, SecurityServiceException {
    addPermissions(administrator, GLOBAL_REFERENCE_DATA_READ);

    authenticateUser(administrator);

    tester.startPage(ReferenceDataPage.class);
    tester.assertRenderedPage(ReferenceDataPage.class);
  }

  /*
   * WicketTester does not pass through Spring Security, so accessibility test is not relevant
   * except when we use an @AuthorizeInstantiation annotation (for example on ReferenceDataTemplate)
   */
  @Test
  void accessUserUnauthorized() throws ServiceException, SecurityServiceException {
    authenticateUser(basicUser);

    assertThrows(
        UnauthorizedInstantiationException.class,
        () -> tester.executeUrl("./reference-data/") // equals to startPage(ReferenceDataPage.class)
        );
  }
}
