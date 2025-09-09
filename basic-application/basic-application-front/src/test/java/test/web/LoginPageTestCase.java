package test.web;

import basicapp.front.navigation.page.HomePage;
import basicapp.front.security.login.component.SignInContentPanel;
import basicapp.front.security.login.component.SignInFooterPanel;
import basicapp.front.security.login.page.SignInPage;
import basicapp.front.security.password.page.SecurityPasswordRecoveryRequestCreationPage;
import basicapp.front.security.password.page.SecurityPasswordRecoveryRequestResetPage;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.Test;
import test.web.config.spring.SpringBootTestBasicApplicationWebapp;

@SpringBootTestBasicApplicationWebapp
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
  void formSubmitSuccess() {
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
  void formSubmitFail() {
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
