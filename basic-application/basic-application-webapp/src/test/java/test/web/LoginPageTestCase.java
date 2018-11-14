package test.web;

import org.apache.wicket.util.tester.FormTester;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.basicapp.web.application.navigation.page.HomePage;
import org.iglooproject.basicapp.web.application.security.login.component.SignInContentPanel;
import org.iglooproject.basicapp.web.application.security.login.component.SignInFooterPanel;
import org.iglooproject.basicapp.web.application.security.login.page.SignInPage;
import org.iglooproject.basicapp.web.application.security.password.page.SecurityPasswordRecoveryPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.Test;

public class LoginPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void loginPage() {
		tester.startPage(SignInPage.class);
		tester.assertRenderedPage(SignInPage.class);
		
		tester.assertComponent("content", SignInContentPanel.class);
	}

	@Test
	public void loginPagePasswordRecovery() {
		tester.startPage(SignInPage.class);
		tester.assertRenderedPage(SignInPage.class);
		
		tester.assertComponent("content", SignInContentPanel.class);
		tester.assertComponent("footer", SignInFooterPanel.class);
		
		tester.assertEnabled("footer:passwordRecovery");
		tester.clickLink("footer:passwordRecovery");
		
		tester.assertRenderedPage(SecurityPasswordRecoveryPage.class);
	}

	@Test
	public void loginPageFormSuccess() throws ServiceException, SecurityServiceException {
		String username = "admin";
		String firstname = "Kobalt";
		String lastname = "Lyon";
		String password = "kobalt";
		createUser(username, firstname, lastname, password,  null, null, null);
		
		tester.startPage(SignInPage.class);
		tester.startComponentInPage(new SignInContentPanel<>("content", UserTypeDescriptor.USER));
		tester.assertRequired("content:form:username");
		tester.assertRequired("content:form:password");
		
		FormTester form = tester.newFormTester("content:form");
		
		form.setValue(form.getForm().get("username"), username);
		form.setValue(form.getForm().get("password"), password);
		
		form.submit();
		
		tester.assertRenderedPage(HomePage.class);
	}

	@Test
	public void loginPageFormFail() throws ServiceException, SecurityServiceException {
		String username = "admin";
		String firstname = "Kobalt";
		String lastname = "Lyon";
		String password = "kobalt";
		createUser(username, firstname, lastname, password, null, null, null);
		
		tester.startPage(SignInPage.class);
		tester.startComponentInPage(new SignInContentPanel<>("content", UserTypeDescriptor.USER));
		tester.assertRequired("content:form:username");
		tester.assertRequired("content:form:password");
		
		FormTester form = tester.newFormTester("content:form");
		
		form.setValue(form.getForm().get("username"), username);
		form.setValue(form.getForm().get("password"), "wrongPassword");
		
		form.submit();
		
		tester.assertErrorMessages(localize("signIn.error.authentication"));
		tester.assertRenderedPage(SignInPage.class);
	}
}
