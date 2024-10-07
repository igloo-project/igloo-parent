package test.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.search.UserSort;
import basicapp.front.user.form.UserAjaxDropDownSingleChoice;
import basicapp.front.user.page.BasicUserDetailPage;
import basicapp.front.user.page.BasicUserListPage;
import basicapp.front.user.page.TechnicalUserListPage;
import igloo.wicket.component.CountLabel;
import java.util.Objects;
import org.apache.wicket.util.tester.FormTester;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceGridView;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreLabelLinkColumnPanel;
import org.junit.jupiter.api.Disabled;
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
  @Disabled("n'est plus utile car plus de usergroup, a modifier pour checker le quicksearch ?")
  public void dataTableBuilderFiltersDropDown() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(BasicUserListPage.class);
    tester.assertRenderedPage(BasicUserListPage.class);

    tester.assertVisible("results", DecoratedCoreDataTablePanel.class);
    @SuppressWarnings("unchecked")
    DecoratedCoreDataTablePanel<User, ?> results =
        (DecoratedCoreDataTablePanel<User, ?>) tester.getComponentFromLastRenderedPage("results");
    assertThat(results.getItemCount()).isEqualTo(2);

    FormTester form = tester.newFormTester("search:form");

    // TODO voir comment on peut ajouter une valeur dans un AjaxDropDown et la selectionnée
    UserAjaxDropDownSingleChoice userQuickSearch =
        (UserAjaxDropDownSingleChoice)
            form.getForm()
                .streamChildren()
                .filter(children -> Objects.equals(children.getId(), "quickAccess"))
                .findFirst()
                .orElse(null);
    form.setValue(userQuickSearch, "basicUser2");

    form.submit();

    assertThat(results.getItemCount()).isEqualTo(1);
  }

  @Test
  void accessToDetail() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(BasicUserListPage.class);
    tester.assertRenderedPage(BasicUserListPage.class);

    tester.assertVisible("results:dataTableContainer:dataTable:body:rows", SequenceGridView.class);
    @SuppressWarnings("unchecked")
    SequenceGridView<User> rows =
        (SequenceGridView<User>)
            tester.getComponentFromLastRenderedPage(
                "results:dataTableContainer:dataTable:body:rows");
    assertTrue(rows.getItems().hasNext());

    String userRowPath = rows.getItems().next().getPageRelativePath();

    tester.assertVisible(userRowPath + ":cells:2:cell", CoreLabelLinkColumnPanel.class);
    @SuppressWarnings("unchecked")
    CoreLabelLinkColumnPanel<User, UserSort> usernameCell =
        (CoreLabelLinkColumnPanel<User, UserSort>)
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
