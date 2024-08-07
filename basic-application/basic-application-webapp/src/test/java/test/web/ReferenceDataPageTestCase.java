package test.web;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.iglooproject.basicapp.web.application.referencedata.page.ReferenceDataPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
class ReferenceDataPageTestCase extends AbstractBasicApplicationWebappTestCase {

  @Test
  void initPage() throws ServiceException, SecurityServiceException {
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
