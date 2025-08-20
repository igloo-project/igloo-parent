package test.web;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_READ;

import basicapp.front.profile.page.ProfilePage;
import basicapp.front.user.page.TechnicalUserListPage;
import org.apache.wicket.Component;
import org.apache.wicket.util.tester.FormTester;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.markup.html.form.LocaleDropDownChoice;
import org.junit.jupiter.api.Test;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import test.web.config.BasicApplicationFrontSpringBootTest;

@BasicApplicationFrontSpringBootTest
class ValidatorTestCase extends AbstractBasicApplicationFrontTestCase {

  /** Test the UserPasswordValidator when username = password which shouldn't be allowed */
  @Test
  void technicalUserPasswordValidator() throws ServiceException, SecurityServiceException {
    addPermissions(administrator, GLOBAL_USER_READ);

    authenticateUser(administrator);

    tester.startPage(TechnicalUserListPage.class);
    tester.assertRenderedPage(TechnicalUserListPage.class);

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

  @Test
  void basicUserPasswordValidator() throws ServiceException, SecurityServiceException {
    authenticateUser(basicUser);

    tester.startPage(ProfilePage.class);
    tester.assertRenderedPage(ProfilePage.class);

    // Open popup
    tester.executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());

    String addPopupPath = tester.modalPath("description:passwordEditPopup");
    String addPopupFormPath = tester.modalFormPath("description:passwordEditPopup");

    FormTester form = tester.newFormTester(addPopupFormPath);

    // Necessary because the submission button is outside the form
    Component submitButton = tester.getComponentFromLastRenderedPage(addPopupPath + ":footer:save");

    form.setValue(form.getForm().get("oldPasswordContainer:oldPassword"), USER_PASSWORD);
    form.setValue(form.getForm().get("newPassword"), "1234");

    form.submit(submitButton);

    tester.assertErrorMessages(
        "Le mot de passe doit contenir au minimum 10 caractères.",
        "Le mot de passe n'est pas valide.");
  }
}
