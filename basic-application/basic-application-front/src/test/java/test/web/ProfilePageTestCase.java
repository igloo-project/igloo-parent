package test.web;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.util.tester.FormTester;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import basicapp.front.administration.form.UserPasswordEditPopup;
import basicapp.front.profile.page.ProfilePage;
import igloo.wicket.component.CoreLabel;
import test.web.config.spring.SpringBootTestBasicApplicationWebapp;

@SpringBootTestBasicApplicationWebapp
class ProfilePageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	void initPage() throws ServiceException, SecurityServiceException {
		authenticateUser(basicUser);
		
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
	}

	@Test
	void updatePasswordPanelComponents() throws ServiceException, SecurityServiceException {
		authenticateUser(basicUser);
		
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
		
		tester.assertVisible("description:passwordEditPopup", UserPasswordEditPopup.class);
		
		tester.assertVisible("description:passwordEditPopup:container");
		Component container = tester.getComponentFromLastRenderedPage("description:passwordEditPopup:container");
		
		// The elements present in AbstractModalPopupPanel.html should be visible (such as the header)
		// Header
		tester.assertVisible(passwordEditPopupPath() + ":header", CoreLabel.class);
		tester.assertLabel(passwordEditPopupPath() + ":header", localize("administration.user.action.password.edit.title"));
		// Body elements should be invisible
		tester.assertInvisible(passwordEditPopupFormPath());
		// Footer elements should be invisible
		tester.assertInvisible(passwordEditPopupPath() + ":footer:save");
		tester.assertInvisible(passwordEditPopupPath() + ":footer:cancel");
		
		tester.executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());
		tester.assertComponentOnAjaxResponse(container);
		
		// Body elements should now be visible
		tester.assertVisible(passwordEditPopupFormPath());
		tester.assertVisible(passwordEditPopupFormPath() + ":oldPasswordContainer:oldPassword");
		tester.assertRequired(passwordEditPopupFormPath() + ":oldPasswordContainer:oldPassword");
		tester.assertVisible(passwordEditPopupFormPath() + ":newPassword");
		tester.assertRequired(passwordEditPopupFormPath() + ":newPassword");
		// Footer elements should now be visible
		tester.assertEnabled(passwordEditPopupPath() + ":footer:save");
		tester.assertEnabled(passwordEditPopupPath() + ":footer:cancel");
	}

	@Test
	void updatePasswordFormMissingRequiredFields() throws ServiceException, SecurityServiceException {
		authenticateUser(basicUser);
		
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
		
		tester.executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());
		
		FormTester form = tester.newFormTester(passwordEditPopupFormPath());
		// Necessary because the submission button is outside the form
		Component submitButton = tester.getComponentFromLastRenderedPage(passwordEditPopupPath() + ":footer:save");
		form.submit(submitButton);
		
		String oldPasswordRequired ="Le champ 'Ancien mot de passe' est obligatoire.";
		String newPasswordRequired ="Le champ 'Nouveau mot de passe' est obligatoire.";
		tester.assertErrorMessages(oldPasswordRequired, newPasswordRequired);
	}

	@Test
	void updatePasswordFormSuccess() throws ServiceException, SecurityServiceException {
		authenticateUser(basicUser);
		
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
		
		tester.executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());
		
		FormTester form = tester.newFormTester(passwordEditPopupFormPath());
		// Necessary because the submission button is outside the form
		Component submitButton = tester.getComponentFromLastRenderedPage(passwordEditPopupPath() + ":footer:save");
		String newPassword = "newPassword";
		form.setValue(form.getForm().get("oldPasswordContainer:oldPassword"), USER_PASSWORD);
		form.setValue(form.getForm().get("newPassword"), newPassword);
		form.submit(submitButton);
		
		tester.assertNoErrorMessage();
		tester.assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.SUCCESS), localize("common.success"));
		
		assertTrue(passwordEncoder.matches(newPassword, userService.getByUsername("basicUser").getPasswordHash()));
	}

	private String passwordEditPopupPath() {
		return tester.modalPath("description:passwordEditPopup");
	}

	private String passwordEditPopupFormPath() {
		return tester.modalFormPath("description:passwordEditPopup");
	}
}
