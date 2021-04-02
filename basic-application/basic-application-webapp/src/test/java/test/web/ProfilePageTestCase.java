package test.web;

import static org.junit.Assert.assertTrue;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.util.tester.FormTester;
import org.iglooproject.basicapp.web.application.administration.form.UserPasswordEditPopup;
import org.iglooproject.basicapp.web.application.profile.page.ProfilePage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.wicketstuff.wiquery.core.events.MouseEvent;

@EnableWebSecurity
@Ignore
public class ProfilePageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void initPage() throws ServiceException, SecurityServiceException {
		authenticateUser(basicUser);
		
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
	}

	@Test
	public void updatePasswordPanelComponents() throws ServiceException, SecurityServiceException {
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
		tester.assertVisible(passwordEditPopupFormPath() + ":oldPassword");
		tester.assertRequired(passwordEditPopupFormPath() + ":oldPassword");
		tester.assertVisible(passwordEditPopupFormPath() + ":newPassword");
		tester.assertRequired(passwordEditPopupFormPath() + ":newPassword");
		tester.assertVisible(passwordEditPopupFormPath() + ":confirmPassword");
		tester.assertRequired(passwordEditPopupFormPath() + ":confirmPassword");
		// Footer elements should now be visible
		tester.assertEnabled(passwordEditPopupPath() + ":footer:save");
		tester.assertEnabled(passwordEditPopupPath() + ":footer:cancel");
	}

	@Test
	public void updatePasswordFormMissingRequiredFields() throws ServiceException, SecurityServiceException {
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
		String confirmPasswordRequired ="Le champ 'Confirmation' est obligatoire.";
		tester.assertErrorMessages(oldPasswordRequired, newPasswordRequired, confirmPasswordRequired);
	}

	@Test
	public void updatePasswordFormSuccess() throws ServiceException, SecurityServiceException {
		authenticateUser(basicUser);
		
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
		
		tester.executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());
		
		FormTester form = tester.newFormTester(passwordEditPopupFormPath());
		// Necessary because the submission button is outside the form
		Component submitButton = tester.getComponentFromLastRenderedPage(passwordEditPopupPath() + ":footer:save");
		String newPassword = "newPassword";
		form.setValue(form.getForm().get("oldPassword"), USER_PASSWORD);
		form.setValue(form.getForm().get("newPassword"), newPassword);
		form.setValue(form.getForm().get("confirmPassword"), newPassword);
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
