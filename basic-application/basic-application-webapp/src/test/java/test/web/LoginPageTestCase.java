package test.web;

import org.apache.wicket.util.tester.FormTester;
import org.iglooproject.basicapp.web.application.navigation.page.HomePage;
import org.iglooproject.basicapp.web.application.security.login.component.SignInContentPanel;
import org.iglooproject.basicapp.web.application.security.login.component.SignInFooterPanel;
import org.iglooproject.basicapp.web.application.security.login.page.SignInPage;
import org.iglooproject.basicapp.web.application.security.password.page.SecurityPasswordRecoveryRequestCreationPage;
import org.iglooproject.basicapp.web.application.security.password.page.SecurityPasswordRecoveryRequestResetPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.Test;

class LoginPageTestCase extends AbstractBasicApplicationWebappTestCase {

  @Test
  void initPage() {
    tester.startPage(SignInPage.class);
    tester.assertRenderedPage(SignInPage.class);

    tester.assertVisible("content", SignInContentPanel.class);
    tester.assertVisible("footer", SignInFooterPanel.class);
  }

  @Test
  void redirectionToPasswordRecoveryRequestReset() {
    tester.startPage(SignInPage.class);
    tester.assertRenderedPage(SignInPage.class);

    tester.assertEnabled("footer:passwordRecoveryRequestReset");
    tester.clickLink("footer:passwordRecoveryRequestReset");

    tester.assertRenderedPage(SecurityPasswordRecoveryRequestResetPage.class);
  }

  @Test
  void redirectionToPasswordRecoveryRequestCreation() {
    tester.startPage(SignInPage.class);
    tester.assertRenderedPage(SignInPage.class);

    tester.assertEnabled("footer:passwordRecoveryRequestCreation");
    tester.clickLink("footer:passwordRecoveryRequestCreation");

    tester.assertRenderedPage(SecurityPasswordRecoveryRequestCreationPage.class);
  }

  @Test
  void formSubmitSuccess() throws ServiceException, SecurityServiceException {
    tester.startPage(SignInPage.class);
    tester.assertRenderedPage(SignInPage.class);

    tester.startComponentInPage(new SignInContentPanel("content"));
    tester.assertRequired("content:form:username");
    tester.assertRequired("content:form:password");

    FormTester form = tester.newFormTester("content:form");

    form.setValue(form.getForm().get("username"), basicUser.getUsername());
    form.setValue(form.getForm().get("password"), USER_PASSWORD);

    form.submit();

    tester.assertRenderedPage(HomePage.class);
  }

  @Test
  void formSubmitFail() throws ServiceException, SecurityServiceException {
    tester.startPage(SignInPage.class);
    tester.assertRenderedPage(SignInPage.class);

    tester.startComponentInPage(new SignInContentPanel("content"));
    tester.assertRequired("content:form:username");
    tester.assertRequired("content:form:password");

    FormTester form = tester.newFormTester("content:form");

    form.setValue(form.getForm().get("username"), basicUser.getUsername());
    form.setValue(form.getForm().get("password"), "wrongPassword");

    form.submit();

    tester.assertErrorMessages(localize("signIn.error.authentication"));
    tester.assertRenderedPage(SignInPage.class);
  }
}
