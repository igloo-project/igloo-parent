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
		getWicketTester().startPage(SignInPage.class);
		getWicketTester().assertRenderedPage(SignInPage.class);
		
		getWicketTester().assertComponent("content", SignInContentPanel.class);
	}

	@Test
	public void loginPagePasswordRecovery() {
		getWicketTester().startPage(SignInPage.class);
		getWicketTester().assertRenderedPage(SignInPage.class);
		
		getWicketTester().assertComponent("content", SignInContentPanel.class);
		getWicketTester().assertComponent("footer", SignInFooterPanel.class);
		
		getWicketTester().assertEnabled("footer:passwordRecovery");
		getWicketTester().clickLink("footer:passwordRecovery");
		
		getWicketTester().assertRenderedPage(SecurityPasswordRecoveryPage.class);
	}

	@Test
	public void loginPageFormSuccess() throws ServiceException, SecurityServiceException {
		String username = "admin";
		String firstname = "Kobalt";
		String lastname = "Lyon";
		String password = "kobalt";
		createUser(username, firstname, lastname, password);
		
		getWicketTester().startPage(SignInPage.class);
		getWicketTester().startComponentInPage(new SignInContentPanel<>("content", UserTypeDescriptor.USER));
		getWicketTester().assertRequired("content:form:username");
		getWicketTester().assertRequired("content:form:password");
		
		FormTester form = getWicketTester().newFormTester("content:form");
		
		form.setValue(form.getForm().get("username"), username);
		form.setValue(form.getForm().get("password"), password);
		
		form.submit();
		
		getWicketTester().assertRenderedPage(HomePage.class);
	}

	@Test
	public void loginPageFormFail() throws ServiceException, SecurityServiceException {
		String username = "admin";
		String firstname = "Kobalt";
		String lastname = "Lyon";
		String password = "kobalt";
		createUser(username, firstname, lastname, password);
		
		getWicketTester().startPage(SignInPage.class);
		getWicketTester().startComponentInPage(new SignInContentPanel<>("content", UserTypeDescriptor.USER));
		getWicketTester().assertRequired("content:form:username");
		getWicketTester().assertRequired("content:form:password");
		
		FormTester form = getWicketTester().newFormTester("content:form");
		
		form.setValue(form.getForm().get("username"), username);
		form.setValue(form.getForm().get("password"), "wrongPassword");
		
		form.submit();
		
		getWicketTester().assertErrorMessages(localize("signIn.error.authentication"));
		getWicketTester().assertRenderedPage(SignInPage.class);
	}
}
