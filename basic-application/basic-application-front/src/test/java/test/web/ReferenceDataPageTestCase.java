package test.web;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static org.junit.jupiter.api.Assertions.assertThrows;

import basicapp.front.referencedata.page.ReferenceDataPage;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.junit.jupiter.api.Test;
import test.web.config.spring.SpringBootTestBasicApplicationWebapp;

@SpringBootTestBasicApplicationWebapp
class ReferenceDataPageTestCase extends AbstractBasicApplicationWebappTestCase {

  @Test
  void initPage() {
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
  void accessUserUnauthorized() {
    authenticateUser(basicUser);

    assertThrows(
        UnauthorizedInstantiationException.class,
        () -> tester.executeUrl("./reference-data/") // equals to startPage(ReferenceDataPage.class)
        );
  }
}
