package test.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.wicket.Component;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TagTester;
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
import org.iglooproject.wicket.markup.html.basic.CountLabel;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceGridView;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreLabelLinkColumnPanel;
import org.junit.Test;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity
public class AdministrationBasicUserListPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void initPage() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		tester.startPage(AdministrationBasicUserListPage.class);
		tester.assertRenderedPage(AdministrationBasicUserListPage.class);
	}

	@Test
	public void dataTableBuilderCountZero() throws ServiceException, SecurityServiceException {
		testCountLabel("Aucun utilisateur");
	}

	@Test
	public void dataTableBuilderCountOne() throws ServiceException, SecurityServiceException {
		createUser("user", "firstname", "lastname", "password",
			UserTypeDescriptor.BASIC_USER, null, Sets.newTreeSet(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		
		testCountLabel("1 utilisateur");
	}

	@Test
	public void dataTableBuilderCountMultiple() throws ServiceException, SecurityServiceException {
		createUser("user1", "firstname1", "lastname1", "password1",
			UserTypeDescriptor.BASIC_USER, null, Sets.newTreeSet(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		createUser("user2", "firstname2", "lastname2", "password2",
			UserTypeDescriptor.BASIC_USER, null, Sets.newTreeSet(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		
		testCountLabel("2 utilisateurs");
	}

	private void testCountLabel(String label) throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		tester.startPage(AdministrationBasicUserListPage.class);
		tester.assertRenderedPage(AdministrationBasicUserListPage.class);
		
		tester.assertComponent("results:headingAddInContainer:leftAddInWrapper:leftAddIn:1", CountLabel.class);
		tester.assertLabel("results:headingAddInContainer:leftAddInWrapper:leftAddIn:1", label);
	}

	@Test
	public void dataTableBuilderFiltersDropDown() throws ServiceException, SecurityServiceException {
		initUserGroups();
		UserGroup administrators = userGroupService.getByName("Administrators");
		createUser("user1", "firstname1", "lastname1", "password1",
			UserTypeDescriptor.BASIC_USER, Sets.newTreeSet(administrators), Sets.newTreeSet(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		createUser("user2", "firstname2", "lastname2", "password2",
			UserTypeDescriptor.BASIC_USER, null, Sets.newTreeSet(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		tester.startPage(AdministrationBasicUserListPage.class);
		tester.assertRenderedPage(AdministrationBasicUserListPage.class);
		
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
	public void accessToDetail() throws ServiceException, SecurityServiceException {
		createUser("user", "firstname", "lastname", "password", UserTypeDescriptor.BASIC_USER, null, null);
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		tester.startPage(AdministrationBasicUserListPage.class);
		tester.assertRenderedPage(AdministrationBasicUserListPage.class);
		
		tester.assertComponent("results:dataTableContainer:dataTable:body:rows", SequenceGridView.class);
		@SuppressWarnings("unchecked")
		SequenceGridView<User> rows = (SequenceGridView<User>) tester.getComponentFromLastRenderedPage("results:dataTableContainer:dataTable:body:rows");
		assertTrue(rows.getItems().hasNext());
		
		String userRowPath = rows.getItems().next().getPageRelativePath();
		
		tester.assertComponent(userRowPath + ":cells:2:cell", CoreLabelLinkColumnPanel.class);
		@SuppressWarnings("unchecked")
		CoreLabelLinkColumnPanel<User, UserSort> usernameCell = (CoreLabelLinkColumnPanel<User, UserSort>) tester.getComponentFromLastRenderedPage(userRowPath + ":cells:2:cell");

		tester.clickLink(usernameCell.getPageRelativePath() + ":link");
		
		tester.assertRenderedPage(AdministrationBasicUserDetailPage.class);
	}

	@Test
	public void excelButtonTootilp() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		tester.startPage(AdministrationBasicUserListPage.class);
		tester.assertRenderedPage(AdministrationBasicUserListPage.class);
		
		tester.assertVisible("headerElementsSection:actionsContainer:exportExcel");
		
		Component exportExcel = tester.getComponentFromLastRenderedPage("headerElementsSection:actionsContainer:exportExcel");
		TagTester tagTester = TagTester.createTagByAttribute(tester.getLastResponse().getDocument(), "id", exportExcel.getMarkupId());
		assertEquals(tagTester.getAttribute("title"), localize("common.action.export.excel"));
	}
}
