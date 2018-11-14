package test.web;

import org.apache.wicket.Component;
import org.iglooproject.basicapp.web.application.common.template.theme.basic.NavbarPanel;
import org.iglooproject.basicapp.web.application.navigation.page.HomePage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.bootstrap4.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.junit.Test;

public class HomePageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void homePage() {
		getWicketTester().startPage(HomePage.class);
		
		getWicketTester().assertRenderedPage(HomePage.class);
		
		getWicketTester().assertComponent("pageTitle", CoreLabel.class);
		getWicketTester().assertLabel("pageTitle", localize("home.pageTitle"));
	}

	@Test
	public void homePageComponentsAuthenticated() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_AUTHENTICATED);
		
		getWicketTester().startPage(HomePage.class);
		
		getWicketTester().assertVisible("profile");
		getWicketTester().assertEnabled("profile");
		
		getWicketTester().assertInvisible("users");
		getWicketTester().assertInvisible("referenceData");
	}

	@Test
	public void navbarAuthenticated() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_AUTHENTICATED);
		navBarComponents();
	}

	@Test
	public void navbarAdmin() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		navBarComponents();
	}

	@Test
	public void navBarNavigation() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		getWicketTester().startPage(HomePage.class);
		
		Component consoleNavItem = getWicketTester().getComponentFromLastRenderedPage("navbar:mainNav:" + NavbarItem.CONSOLE.getOrder());
		getWicketTester().clickLink(consoleNavItem.getPageRelativePath() + ":navLink");
		getWicketTester().assertRenderedPage(ConsoleMaintenanceSearchPage.class);
	}

	private void navBarComponents() throws ServiceException, SecurityServiceException {
		getWicketTester().startPage(HomePage.class);
		
		getWicketTester().assertComponent("navbar", NavbarPanel.class);
		
		for (NavbarItem navbarItem : NavbarItem.values()) {
			if (authenticationService.hasRole(navbarItem.getAuthority())) {
				getWicketTester().assertVisible("navbar:mainNav:" + navbarItem.getOrder());
				getWicketTester().assertEnabled("navbar:mainNav:" + navbarItem.getOrder() + ":navLink");
				getWicketTester().assertLabel("navbar:mainNav:" + navbarItem.getOrder() + ":navLink:label", navbarItem.getLabel());
			} else {
				getWicketTester().assertInvisible("navbar:mainNav:" + navbarItem.getOrder());
			}
		}
	}

	private enum NavbarItem {
		HOME(localize("navigation.home"), CoreAuthorityConstants.ROLE_AUTHENTICATED, 0),
		REFERENCE_DATA(localize("navigation.referenceData"), CoreAuthorityConstants.ROLE_ADMIN, 1),
		ADMINISTRATION(localize("navigation.administration"), CoreAuthorityConstants.ROLE_ADMIN, 2),
		CONSOLE(localize("navigation.console"), CoreAuthorityConstants.ROLE_ADMIN, 3);
		
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
