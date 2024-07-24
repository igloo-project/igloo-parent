package test.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import basicapp.front.administration.page.AdministrationBasicUserDetailPage;
import basicapp.front.administration.page.AdministrationBasicUserListPage;
import basicapp.front.administration.template.AdministrationUserDetailTemplate;
import org.apache.wicket.markup.html.link.Link;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.markup.html.template.component.BreadCrumbListView;
import org.iglooproject.wicket.more.markup.html.template.component.LinkGeneratorBreadCrumbElementPanel;
import org.iglooproject.wicket.more.markup.html.template.component.SimpleBreadCrumbElementPanel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.junit.jupiter.api.Test;
import test.web.config.spring.SpringBootTestBasicApplicationWebapp;

@SpringBootTestBasicApplicationWebapp
class AdministrationBasicUserDetailPageTestCase extends AbstractBasicApplicationWebappTestCase {

  @Test
  void initPage() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    String url =
        AdministrationUserDetailTemplate.mapper()
            .ignoreParameter2()
            .map(GenericEntityModel.of(basicUser))
            .url();
    tester.executeUrl(url);

    tester.assertRenderedPage(AdministrationBasicUserDetailPage.class);
  }

  @Test
  void breadcrumb() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    String url =
        AdministrationUserDetailTemplate.mapper()
            .ignoreParameter2()
            .map(GenericEntityModel.of(basicUser))
            .url();
    tester.executeUrl(url);

    tester.assertRenderedPage(AdministrationBasicUserDetailPage.class);

    tester.assertVisible(tester.breadCrumbPath(), BreadCrumbListView.class);

    String administrationBreadCrumbPath = tester.breadCrumbElementPath(0);
    tester.assertVisible(administrationBreadCrumbPath, SimpleBreadCrumbElementPanel.class);

    String administrationBasicUserBreadCrumbPath = tester.breadCrumbElementPath(1);
    tester.assertVisible(
        administrationBasicUserBreadCrumbPath, LinkGeneratorBreadCrumbElementPanel.class);
    tester.assertEnabled(
        administrationBasicUserBreadCrumbPath + ":breadCrumbElementLink", Link.class);
    @SuppressWarnings("unchecked")
    Link<Void> administrationBasicUserLink =
        (Link<Void>)
            tester.getComponentFromLastRenderedPage(
                administrationBasicUserBreadCrumbPath + ":breadCrumbElementLink");
    String administrationBasicUserLabel =
        (String) administrationBasicUserLink.getBody().getObject();
    assertEquals(
        localize("navigation.administration.user.basicUser"), administrationBasicUserLabel);

    tester.clickLink(administrationBasicUserBreadCrumbPath + ":breadCrumbElementLink");
    tester.assertRenderedPage(AdministrationBasicUserListPage.class);
  }

  @Test
  void desactivateUser() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    String url =
        AdministrationUserDetailTemplate.mapper()
            .ignoreParameter2()
            .map(GenericEntityModel.of(basicUser))
            .url();
    tester.executeUrl(url);

    assertTrue(basicUser.isEnabled());

    tester.assertInvisible("headerElementsSection:actionsContainer:enable");
    tester.assertEnabled("headerElementsSection:actionsContainer:disable");

    tester.executeAjaxEvent("headerElementsSection:actionsContainer:disable", "confirm.bs.confirm");

    tester.assertEnabled("headerElementsSection:actionsContainer:enable");
    tester.assertInvisible("headerElementsSection:actionsContainer:disable");

    assertFalse(basicUser.isEnabled());
  }
}
