package test.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.wicket.util.tester.FormTester;
import org.assertj.core.util.Sets;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.search.UserSort;
import org.iglooproject.basicapp.web.application.administration.form.UserGroupDropDownSingleChoice;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserDetailPage;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserListPage;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceGridView;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreLabelLinkColumnPanel;
import org.junit.Test;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity
public class AdministrationBasicUserListPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void administrationBasicUserListPage() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		tester.startPage(AdministrationBasicUserListPage.class);
		
		tester.assertRenderedPage(AdministrationBasicUserListPage.class);
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
		
		tester.startPage(AdministrationBasicUserListPage.class);
		
		tester.assertComponent("results", DecoratedCoreDataTablePanel.class);
		@SuppressWarnings("unchecked")
		DecoratedCoreDataTablePanel<User, ?> results = (DecoratedCoreDataTablePanel<User, ?>) tester.getComponentFromLastRenderedPage("results");
		assertEquals(2, results.getItemCount(), 0);
		
		FormTester form = tester.newFormTester("search:form");
		UserGroupDropDownSingleChoice userGroupField = (UserGroupDropDownSingleChoice) form.getForm().get("userGroup");
		assertEquals(userGroupField.getChoices().size(), 2);
		form.select(userGroupField.getId(), 0); // It should be Administrators
		
		form.submit();
		
		assertTrue(administrators.equals(userGroupField.getModelObject()));
		assertEquals(1, results.getItemCount(), 0);
	}

	@Test
	public void administrationBasicUserListGoToDetail() throws ServiceException, SecurityServiceException {
		createUser("user", "firstname", "lastname", "password", UserTypeDescriptor.BASIC_USER, null, null);
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		tester.startPage(AdministrationBasicUserListPage.class);
		
		tester.assertComponent("results:dataTableContainer:dataTable:body:rows", SequenceGridView.class);
		@SuppressWarnings("unchecked")
		SequenceGridView<User> rows = (SequenceGridView<User>) tester.getComponentFromLastRenderedPage("results:dataTableContainer:dataTable:body:rows");
		if (!rows.getItems().hasNext()) {
			return;
		}
		
		String userRowPath = rows.getItems().next().getPageRelativePath();
		
		tester.assertComponent(userRowPath + ":cells:2:cell", CoreLabelLinkColumnPanel.class);
		@SuppressWarnings("unchecked")
		CoreLabelLinkColumnPanel<User, UserSort> usernameCell = (CoreLabelLinkColumnPanel<User, UserSort>) tester.getComponentFromLastRenderedPage(userRowPath + ":cells:2:cell");

		tester.clickLink(usernameCell.getPageRelativePath() + ":link");
		
		tester.assertRenderedPage(AdministrationBasicUserDetailPage.class);
	}
}
