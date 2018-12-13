package test.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.wicket.markup.html.link.Link;
import org.assertj.core.util.Sets;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.typedescriptor.UserTypeDescriptor;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserDetailPage;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserListPage;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.markup.html.template.component.BreadCrumbListView;
import org.iglooproject.wicket.more.markup.html.template.component.LinkGeneratorBreadCrumbElementPanel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.junit.Test;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity
public class AdministrationBasicUserDetailPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void initPage() throws ServiceException, SecurityServiceException {
		initBasicUserAndStartPage();
		
		tester.assertRenderedPage(AdministrationBasicUserDetailPage.class);
	}

	@Test
	public void breadcrumb() throws ServiceException, SecurityServiceException {
		initBasicUserAndStartPage();
		
		tester.assertRenderedPage(AdministrationBasicUserDetailPage.class);
		
		tester.assertVisible(breadCrumbPath());
		tester.assertComponent(breadCrumbPath(), BreadCrumbListView.class);
		
		tester.assertVisible(breadCrumbElementPath(0));
		tester.assertComponent(breadCrumbElementPath(0), LinkGeneratorBreadCrumbElementPanel.class);
		tester.assertVisible(breadCrumbElementPath(0) + ":breadCrumbElementLink");
		tester.assertEnabled(breadCrumbElementPath(0) + ":breadCrumbElementLink");
		tester.assertComponent(breadCrumbElementPath(0) + ":breadCrumbElementLink", Link.class);
		@SuppressWarnings("unchecked")
		Link<Void> administrationLink = (Link<Void>) tester.getComponentFromLastRenderedPage(breadCrumbElementPath(0) + ":breadCrumbElementLink");
		String administrationLabel = (String) administrationLink.getBody().getObject();
		assertEquals(administrationLabel, localize("navigation.administration"));
		
		tester.assertVisible(breadCrumbElementPath(1));
		tester.assertComponent(breadCrumbElementPath(1), LinkGeneratorBreadCrumbElementPanel.class);
		tester.assertVisible(breadCrumbElementPath(1) + ":breadCrumbElementLink");
		tester.assertEnabled(breadCrumbElementPath(1) + ":breadCrumbElementLink");
		tester.assertComponent(breadCrumbElementPath(1) + ":breadCrumbElementLink", Link.class);
		@SuppressWarnings("unchecked")
		Link<Void> administrationBasicUserListLink = (Link<Void>) tester.getComponentFromLastRenderedPage(breadCrumbElementPath(1) + ":breadCrumbElementLink");
		String administrationBasicUserListLabel = (String) administrationBasicUserListLink.getBody().getObject();
		assertEquals(administrationBasicUserListLabel, localize("navigation.administration.user.basicUser"));
		
		tester.clickLink(breadCrumbElementPath(0) + ":breadCrumbElementLink");
		tester.assertRenderedPage(AdministrationBasicUserListPage.class);
	}

	@Test
	public void desactivateUser() throws ServiceException, SecurityServiceException {
		User user = initBasicUserAndStartPage();
		
		assertTrue(user.isActive());
		
		tester.assertInvisible("headerElementsSection:actionsContainer:enable");
		tester.assertVisible("headerElementsSection:actionsContainer:disable");
		tester.assertEnabled("headerElementsSection:actionsContainer:disable");
		
		tester.executeAjaxEvent("headerElementsSection:actionsContainer:disable", "confirm");
		
		tester.assertVisible("headerElementsSection:actionsContainer:enable");
		tester.assertEnabled("headerElementsSection:actionsContainer:enable");
		tester.assertInvisible("headerElementsSection:actionsContainer:disable");
		
		assertFalse(user.isActive());
	}

	private User initBasicUserAndStartPage() throws ServiceException, SecurityServiceException {
		User user = createUser("user1", "firstname1", "lastname1", "password1",
				UserTypeDescriptor.BASIC_USER, null, Sets.newTreeSet(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		String url = AdministrationUserDetailTemplate.mapper()
			.ignoreParameter2()
			.map(GenericEntityModel.of(user)).url();
		tester.executeUrl(url);
		
		return user;
	}
}
