package test.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import basicapp.back.business.user.model.BasicUser;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.search.BasicUserSort;
import basicapp.front.user.page.BasicUserDetailPage;
import basicapp.front.user.page.BasicUserListPage;
import basicapp.front.user.page.TechnicalUserListPage;
import basicapp.front.usergroup.form.UserGroupDropDownSingleChoice;
import igloo.wicket.component.CountLabel;
import org.apache.wicket.util.tester.FormTester;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceGridView;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreLabelLinkColumnPanel;
import org.junit.jupiter.api.Test;
import test.web.config.spring.SpringBootTestBasicApplicationWebapp;

@SpringBootTestBasicApplicationWebapp
class BasicUserListPageTestCase extends AbstractBasicApplicationWebappTestCase {

  @Test
  void initPage() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(BasicUserListPage.class);
    tester.assertRenderedPage(BasicUserListPage.class);
  }

  @Test
  void dataTableBuilderCountZero() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(TechnicalUserListPage.class);
    tester.assertRenderedPage(TechnicalUserListPage.class);

    FormTester form = tester.newFormTester("search:form");
    form.setValue("name", "basicUser");
    form.submit();

    tester.assertVisible(
        "results:headingAddInContainer:leftAddInWrapper:leftAddIn:1", CountLabel.class);
    tester.assertLabel(
        "results:headingAddInContainer:leftAddInWrapper:leftAddIn:1", "Aucun utilisateur");
  }

  @Test
  void dataTableBuilderCountOne() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(TechnicalUserListPage.class);
    tester.assertRenderedPage(TechnicalUserListPage.class);

    tester.assertVisible(
        "results:headingAddInContainer:leftAddInWrapper:leftAddIn:1", CountLabel.class);
    tester.assertLabel(
        "results:headingAddInContainer:leftAddInWrapper:leftAddIn:1", "1 utilisateur");
  }

  @Test
  void dataTableBuilderCountMultiple() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(BasicUserListPage.class);
    tester.assertRenderedPage(BasicUserListPage.class);

    tester.assertVisible(
        "results:headingAddInContainer:leftAddInWrapper:leftAddIn:1", CountLabel.class);
    tester.assertLabel(
        "results:headingAddInContainer:leftAddInWrapper:leftAddIn:1", "2 utilisateurs");
  }

  @Test
  void dataTableBuilderFiltersDropDown() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(BasicUserListPage.class);
    tester.assertRenderedPage(BasicUserListPage.class);

    tester.assertVisible("results", DecoratedCoreDataTablePanel.class);
    @SuppressWarnings("unchecked")
    DecoratedCoreDataTablePanel<User, ?> results =
        (DecoratedCoreDataTablePanel<User, ?>) tester.getComponentFromLastRenderedPage("results");
    assertEquals(2, results.getItemCount());

    FormTester form = tester.newFormTester("search:form");
    UserGroupDropDownSingleChoice userGroupField =
        (UserGroupDropDownSingleChoice) form.getForm().get("userGroup");
    assertEquals(2, userGroupField.getChoices().size());
    form.select(userGroupField.getId(), 0); // It should be Administrators

    form.submit();

    assertEquals(administrators, userGroupField.getModelObject());
    assertEquals(0, results.getItemCount());
  }

  @Test
  void accessToDetail() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(BasicUserListPage.class);
    tester.assertRenderedPage(BasicUserListPage.class);

    tester.assertVisible("results:dataTableContainer:dataTable:body:rows", SequenceGridView.class);
    @SuppressWarnings("unchecked")
    SequenceGridView<BasicUser> rows =
        (SequenceGridView<BasicUser>)
            tester.getComponentFromLastRenderedPage(
                "results:dataTableContainer:dataTable:body:rows");
    assertTrue(rows.getItems().hasNext());

    String userRowPath = rows.getItems().next().getPageRelativePath();

    tester.assertVisible(userRowPath + ":cells:2:cell", CoreLabelLinkColumnPanel.class);
    @SuppressWarnings("unchecked")
    CoreLabelLinkColumnPanel<BasicUser, BasicUserSort> usernameCell =
        (CoreLabelLinkColumnPanel<BasicUser, BasicUserSort>)
            tester.getComponentFromLastRenderedPage(userRowPath + ":cells:2:cell");

    tester.clickLink(usernameCell.getPageRelativePath() + ":link");

    tester.assertRenderedPage(BasicUserDetailPage.class);
  }

  @Test
  void excelButtonTootilp() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(BasicUserListPage.class);
    tester.assertRenderedPage(BasicUserListPage.class);

    tester.assertVisible("headerElementsSection:actionsContainer:exportExcel");

    tester.assertTooltip(
        "headerElementsSection:actionsContainer:exportExcel",
        localize("common.action.export.excel"));
  }
}
