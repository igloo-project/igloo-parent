package test.web;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.iglooproject.basicapp.web.application.common.template.theme.basic.NavbarPanel;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.basicapp.web.application.navigation.page.HomePage;
import org.iglooproject.basicapp.web.application.security.login.component.SignInContentPanel;
import org.iglooproject.basicapp.web.application.security.login.component.SignInFooterPanel;
import org.iglooproject.basicapp.web.application.security.login.page.SignInPage;
import org.iglooproject.basicapp.web.application.security.password.page.SecurityPasswordRecoveryPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.wicket.more.AbstractWicketMoreTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import test.web.config.spring.BasicApplicationWebappTestCommonConfig;

@ContextConfiguration(classes = BasicApplicationWebappTestCommonConfig.class)
public class AbstractBasicApplicationWebappTestCase extends AbstractWicketMoreTestCase {

	@Autowired
	WebApplication application;

	@Before
	public void setUp() {
		setWicketTester(new WicketTester(application));
	}

	@Test
	public void homePageRendersSuccessfully() {
		getWicketTester().startPage(HomePage.class);
		
		getWicketTester().assertRenderedPage(HomePage.class);
	}

	@Test
	public void homePageLabelRendersSuccessfully() {
		getWicketTester().startPage(HomePage.class);
		
		// Test contenu d'un label (wicketId, value)
		getWicketTester().assertLabel("pageTitle", "Accueil");
	}

	@Test
	public void homePageLinkRendersSuccessfully() {
		getWicketTester().startPage(HomePage.class);
		
		// Test composants bien initialisé
		getWicketTester().assertComponent("navbar", NavbarPanel.class);
		
		// TODO @mpiva: accéder aux différents composants de la page, notamment à ceux de la navbar
//		Component navbar = getWicketTester().getComponentFromLastRenderedPage("navbar");
//		getWicketTester().assertEnabled("users");
//		getWicketTester().assertEnabled("profile");
//		getWicketTester().assertEnabled("referenceData");
//
//		getWicketTester().assertEnabled(Localizer.get().getString("navigation.console", null));
//		// Test contenu click
//		getWicketTester().clickLink("profile");
		
		//
//		getWicketTester().assertRenderedPage(ProfilePage.class);
	}

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
		
		getWicketTester().clickLink("footer:passwordRecovery");
		
		getWicketTester().assertRenderedPage(SecurityPasswordRecoveryPage.class);
	}

	@Test
	public void loginPageForm() {
		getWicketTester().startComponentInPage(new SignInContentPanel<>("content", UserTypeDescriptor.USER));
		getWicketTester().assertNoFeedbackMessage(0);
		getWicketTester().assertRequired("content:form:username");
		getWicketTester().assertRequired("content:form:password");
		
		FormTester form = getWicketTester().newFormTester("content:form");
		// TODO @mpiva : tester les messages de feedback
		
		form.setValue(form.getForm().get("username"), "admin");
		form.setValue(form.getForm().get("password"), "kobalt");
		// TODO @mpiva : ajouter des données dans la base de données
		
		form.submit();
		
		getWicketTester().assertRenderedPage(HomePage.class);
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		// TODO cleanEntities(service appelé);
	}

}
