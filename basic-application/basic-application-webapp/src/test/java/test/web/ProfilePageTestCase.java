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
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.junit.Test;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.wicketstuff.wiquery.core.events.MouseEvent;


@EnableWebSecurity
public class ProfilePageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void initPage() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_AUTHENTICATED);
		
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
	}

	@Test
	public void updatePasswordPanelComponents() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_AUTHENTICATED);
		
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
		
		tester.assertVisible("description:passwordEditPopup");
		tester.assertComponent("description:passwordEditPopup", UserPasswordEditPopup.class);
		
		tester.assertVisible("description:passwordEditPopup:container");
		Component container = tester.getComponentFromLastRenderedPage("description:passwordEditPopup:container");
		
		// The elements present in AbstractModalPopupPanel.html should be visible (such as the header)
		// Header
		tester.assertVisible(modalPath() + ":header");
		tester.assertComponent(modalPath() + ":header", CoreLabel.class);
		tester.assertLabel(modalPath() + ":header", localize("administration.user.action.password.edit.title"));
		// Body elements should be invisible
		tester.assertInvisible(formPath());
		// Footer elements should be invisible
		tester.assertInvisible(modalPath() + ":footer:save");
		tester.assertInvisible(modalPath() + ":footer:cancel");
		
		tester.executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());
		tester.assertComponentOnAjaxResponse(container);
		
		// Body elements should now be visible
		tester.assertVisible(formPath());
		tester.assertVisible(formPath() + ":oldPassword");
		tester.assertRequired(formPath() + ":oldPassword");
		tester.assertVisible(formPath() + ":newPassword");
		tester.assertRequired(formPath() + ":newPassword");
		tester.assertVisible(formPath() + ":confirmPassword");
		tester.assertRequired(formPath() + ":confirmPassword");
		// Footer elements should now be visible
		tester.assertVisible(modalPath() + ":footer:save");
		tester.assertEnabled(modalPath() + ":footer:save");
		tester.assertVisible(modalPath() + ":footer:cancel");
		tester.assertEnabled(modalPath() + ":footer:cancel");
	}

	@Test
	public void updatePasswordFormMissingRequiredFields() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_AUTHENTICATED);
		
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
		
		tester.executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());
		
		FormTester form = tester.newFormTester(formPath());
		// Necessary because the submission button is outside the form
		Component submitButton = tester.getComponentFromLastRenderedPage(modalPath() + ":footer:save");
		form.submit(submitButton);
		
		String oldPasswordRequired ="Le champ 'Ancien mot de passe' est obligatoire.";
		String newPasswordRequired ="Le champ 'Nouveau mot de passe' est obligatoire.";
		String confirmPasswordRequired ="Le champ 'Confirmation' est obligatoire.";
		tester.assertErrorMessages(oldPasswordRequired, newPasswordRequired, confirmPasswordRequired);
	}

	@Test
	public void updatePasswordFormSuccess() throws ServiceException, SecurityServiceException {
		String username = "username";
		String firstname = "firstname";
		String lastname = "lastname";
		String password = "oldPassword";
		createAndAuthenticateUser(username, firstname, lastname, password, CoreAuthorityConstants.ROLE_AUTHENTICATED);
		
		tester.startPage(ProfilePage.class);
		tester.assertRenderedPage(ProfilePage.class);
		
		tester.executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());
		
		FormTester form = tester.newFormTester(formPath());
		// Necessary because the submission button is outside the form
		Component submitButton = tester.getComponentFromLastRenderedPage(modalPath() + ":footer:save");
		String newPassword = "newPassword";
		form.setValue(form.getForm().get("oldPassword"), password);
		form.setValue(form.getForm().get("newPassword"), newPassword);
		form.setValue(form.getForm().get("confirmPassword"), newPassword);
		form.submit(submitButton);
		
		tester.assertNoErrorMessage();
		tester.assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.SUCCESS), localize("common.success"));
		
		assertTrue(passwordEncoder.matches(newPassword, userService.getByUsername(username).getPasswordHash()));
	}

	private String modalPath() {
		return modalPath("description:passwordEditPopup");
	}

	private String formPath() {
		return modalFormPath("description:passwordEditPopup");
	}
}
