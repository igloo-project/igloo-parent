package test.web;

import org.apache.wicket.Localizer;
import org.iglooproject.basicapp.web.application.common.template.theme.basic.NavbarPanel;
import org.iglooproject.basicapp.web.application.navigation.page.HomePage;
import org.iglooproject.basicapp.web.application.referencedata.page.ReferenceDataPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.junit.Test;

public class HomePageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void homePage() {
		getWicketTester().startPage(HomePage.class);
		
		getWicketTester().assertRenderedPage(HomePage.class);
	}

	@Test
	public void homePageLabel() {
		getWicketTester().startPage(HomePage.class);
		
		// Test contenu d'un label (wicketId, value)
		getWicketTester().assertLabel("pageTitle", "Accueil");
	}

	@Test
	public void homePageReferenceDataPageAccessibilityAdmin() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		getWicketTester().executeUrl("./reference-data/"); // equals to startPage(ReferenceDataPage.class)
		
		getWicketTester().assertRenderedPage(ReferenceDataPage.class);
	}

//	@Test
//	public void homePageReferenceDataPageAccessibilityAuthenticated() throws ServiceException, SecurityServiceException {
//		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_AUTHENTICATED);
//		
//		try {
//			getWicketTester().executeUrl("./reference-data/");
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("unauthorized instanciation");
//		}
//		// TODO @mpiva : tester message de feedback
//		
//		getWicketTester().assertRenderedPage(HomePage.class);
//	}

	@Test
	public void navbarAuthenticated() throws ServiceException, SecurityServiceException {
		navBar(CoreAuthorityConstants.ROLE_AUTHENTICATED);
	}

	@Test
	public void navbarAdmin() throws ServiceException, SecurityServiceException {
		navBar(CoreAuthorityConstants.ROLE_ADMIN);
	}

	private void navBar(String authority) throws ServiceException, SecurityServiceException {
		String username = "admin";
		String firstname = "Kobalt";
		String lastname = "Kobalt";
		String password = "kobalt69";
		createAndAuthenticateUser(username, firstname, lastname, password, authority);
		
		getWicketTester().startPage(HomePage.class);
		
		getWicketTester().assertComponent("navbar", NavbarPanel.class);
		
		for (NavbarItem navbarItem : NavbarItem.values()) {
			if (authenticationService.hasRole(navbarItem.getAuthority())) {
				getWicketTester().assertVisible("navbar:mainNav:"+ navbarItem.getOrder());
				getWicketTester().assertEnabled("navbar:mainNav:"+ navbarItem.getOrder() +":navLink");
				getWicketTester().assertLabel("navbar:mainNav:"+ navbarItem.getOrder() +":navLink:label", navbarItem.getLabel());
			} else {
				getWicketTester().assertInvisible("navbar:mainNav:"+ navbarItem.getOrder());
			}
		}
		
	}

	private enum NavbarItem {
		HOME(Localizer.get().getString("navigation.home", null), CoreAuthorityConstants.ROLE_AUTHENTICATED, 0),
		REFERENCE_DATA(Localizer.get().getString("navigation.referenceData", null), CoreAuthorityConstants.ROLE_ADMIN, 1),
		ADMINISTRATION(Localizer.get().getString("navigation.administration", null), CoreAuthorityConstants.ROLE_ADMIN, 2),
		CONSOLE(Localizer.get().getString("navigation.console", null), CoreAuthorityConstants.ROLE_ADMIN, 3);
		
		private String label;
		
		private String authority;
		
		private int order;
		
		private NavbarItem(String label, String authority, int order) {
			this.label = label;
			this.authority = authority;
			this.order = order;
		}

		public String getLabel() {
			return label;
		}

		public String getAuthority() {
			return authority;
		}

		public int getOrder() {
			return order;
		}
	}
}
