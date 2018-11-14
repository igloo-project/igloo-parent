package test.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.wicket.util.tester.FormTester;
import org.assertj.core.util.Sets;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.web.application.administration.form.UserGroupDropDownSingleChoice;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserListPage;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.junit.Test;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity
public class AdministrationBasicUserListPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void administrationBasicUserListPage() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		getWicketTester().startPage(AdministrationBasicUserListPage.class);
		
		getWicketTester().assertRenderedPage(AdministrationBasicUserListPage.class);
	}

	@Test
	public void administrationBasicUserListPageFilters() throws ServiceException, SecurityServiceException {
		initUserGroups();
		UserGroup administrators = userGroupService.getByName("Administrators");
		createUser("user1", "firstname1", "lastname1", "password1",
				UserTypeDescriptor.BASIC_USER, Sets.newTreeSet(administrators), Sets.newTreeSet(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		createUser("user2", "firstname2", "lastname2", "password2",
				UserTypeDescriptor.BASIC_USER, Sets.newTreeSet(), Sets.newTreeSet(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		getWicketTester().startPage(AdministrationBasicUserListPage.class);
		
		getWicketTester().assertComponent("results", DecoratedCoreDataTablePanel.class);
		@SuppressWarnings("unchecked")
		DecoratedCoreDataTablePanel<User, ?> results = (DecoratedCoreDataTablePanel<User, ?>) getWicketTester().getComponentFromLastRenderedPage("results");
		assertEquals(2, results.getItemCount(), 0);
		
		FormTester form = getWicketTester().newFormTester("search:form");
		UserGroupDropDownSingleChoice userGroupField = (UserGroupDropDownSingleChoice) form.getForm().get("userGroup");
		assertEquals(userGroupField.getChoices().size(), 2);
		form.select(userGroupField.getId(), 0); // It should be Administrators
		
		form.submit();
		
		assertTrue(administrators.equals(userGroupField.getModelObject()));
		assertEquals(1, results.getItemCount(), 0);
	}
}
