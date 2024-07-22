package test.web;

import basicapp.front.administration.page.AdministrationTechnicalUserListPage;
import org.apache.wicket.Component;
import org.apache.wicket.util.tester.FormTester;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.markup.html.form.LocaleDropDownChoice;
import org.junit.jupiter.api.Test;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import test.web.config.spring.SpringBootTestBasicApplicationWebapp;

@SpringBootTestBasicApplicationWebapp
class ValidatorTestCase extends AbstractBasicApplicationWebappTestCase {

  /** Test the UserPasswordValidator when username = password which shouldn't be allowed */
  @Test
  void userPasswordValidator() throws ServiceException, SecurityServiceException {
    authenticateUser(administrator);

    tester.startPage(AdministrationTechnicalUserListPage.class);
    tester.assertRenderedPage(AdministrationTechnicalUserListPage.class);

    // Open popup
    tester.executeAjaxEvent(
        "headerElementsSection:actionsContainer:add", MouseEvent.CLICK.getEventLabel());

    String addPopupPath = tester.modalPath("addPopup");
    String addPopupFormPath = tester.modalFormPath("addPopup");

    FormTester form = tester.newFormTester(addPopupFormPath);

    // Necessary because the submission button is outside the form
    Component submitButton = tester.getComponentFromLastRenderedPage(addPopupPath + ":footer:save");

    form.setValue(form.getForm().get("firstName"), USER_PASSWORD);
    form.setValue(form.getForm().get("lastName"), USER_PASSWORD);
    form.setValue(form.getForm().get("username"), USER_PASSWORD);
    form.setValue(form.getForm().get("addContainer:passwordContainer:password"), USER_PASSWORD);
    LocaleDropDownChoice localeField = (LocaleDropDownChoice) form.getForm().get("locale");
    form.select(localeField.getId(), 0);

    form.submit(submitButton);

    tester.assertErrorMessages("Le mot de passe n'est pas valide.");
  }
}
