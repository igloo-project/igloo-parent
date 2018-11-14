package test.web;

import static org.junit.Assert.assertTrue;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.util.tester.FormTester;
import org.iglooproject.basicapp.web.application.administration.form.UserPasswordUpdatePopup;
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
	public void profilePage() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		getWicketTester().startPage(ProfilePage.class);
		
		getWicketTester().assertRenderedPage(ProfilePage.class);
	}

	@Test
	public void updatePasswordComponents() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_AUTHENTICATED);
		
		getWicketTester().startPage(ProfilePage.class);
		
		getWicketTester().assertVisible("description:passwordEditPopup");
		getWicketTester().assertComponent("description:passwordEditPopup", UserPasswordUpdatePopup.class);
		
		getWicketTester().assertVisible("description:passwordEditPopup:container");
		Component container = getWicketTester().getComponentFromLastRenderedPage("description:passwordEditPopup:container");
		
		// TODO : To use when we find a way to execute js with WicketTester
//		TagTester tagTesterContainerHidden = TagTester.createTagByAttribute(getWicketTester().getLastResponse().getDocument(), "id", container.getMarkupId());
//		assertEquals(tagTesterContainerHidden.getAttribute("style"), "display: none;");
//		assertFalse(tagTesterContainerHidden.getAttributeContains("class", "show"));
		
		// The elements present in AbstractModalPopupPanel.html should be visible (such as the header)
		// Header
		getWicketTester().assertVisible(modalPath() + ":header");
		getWicketTester().assertComponent(modalPath() + ":header", CoreLabel.class);
		getWicketTester().assertLabel(modalPath() + ":header", localize("administration.user.action.password.edit.title"));
		// Body elements should be invisible
		getWicketTester().assertInvisible(formPath());
		// Footer elements should be invisible
		getWicketTester().assertInvisible(modalPath() + ":footer:save");
		getWicketTester().assertInvisible(modalPath() + ":footer:cancel");
		
		getWicketTester().executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());
		getWicketTester().assertComponentOnAjaxResponse(container);
		
		// TODO : To use when we find a way to execute js with WicketTester
//		TagTester tagTesterContainerVisible= TagTester.createTagByAttribute(getWicketTester().getLastResponse().getDocument(), "id", container.getMarkupId());
//		assertEquals(tagTesterContainerVisible.getAttribute("style"), "display: block;");
//		assertTrue(tagTesterContainerVisible.getAttributeContains("class", "show"));
		
		// Body elements should now be visible
		getWicketTester().assertVisible(formPath());
		getWicketTester().assertVisible(formPath() + ":oldPassword");
		getWicketTester().assertRequired(formPath() + ":oldPassword");
		getWicketTester().assertVisible(formPath() + ":newPassword");
		getWicketTester().assertRequired(formPath() + ":newPassword");
		getWicketTester().assertVisible(formPath() + ":confirmPassword");
		getWicketTester().assertRequired(formPath() + ":confirmPassword");
		// Footer elements should now be visible
		getWicketTester().assertVisible(modalPath() + ":footer:save");
		getWicketTester().assertEnabled(modalPath() + ":footer:save");
		getWicketTester().assertVisible(modalPath() + ":footer:cancel");
		getWicketTester().assertEnabled(modalPath() + ":footer:cancel");
	}

	@Test
	public void updatePasswordFormWrongOldPassword() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_AUTHENTICATED);
		
		getWicketTester().startPage(ProfilePage.class);
		
		getWicketTester().executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());
		
		FormTester form = getWicketTester().newFormTester(formPath());
		// Necessary because the submission button is outside the form
		Component submitButton = getWicketTester().getComponentFromLastRenderedPage(modalPath() + ":footer:save");
		form.setValue(form.getForm().get("oldPassword"), "wrongOldPassword");
		form.setValue(form.getForm().get("newPassword"), "newPassword");
		form.setValue(form.getForm().get("confirmPassword"), "newPassword");
		form.submit(submitButton);
		
		getWicketTester().assertErrorMessages(localize("administration.user.action.password.edit.error.oldPassword"));
	}

	@Test
	public void updatePasswordFormSuccess() throws ServiceException, SecurityServiceException {
		String username = "username";
		String firstname = "firstname";
		String lastname = "lastname";
		String password = "oldPassword";
		createAndAuthenticateUser(username, firstname, lastname, password, CoreAuthorityConstants.ROLE_AUTHENTICATED);
		
		getWicketTester().startPage(ProfilePage.class);
		
		getWicketTester().executeAjaxEvent("description:passwordEdit", MouseEvent.CLICK.getEventLabel());
		
		FormTester form = getWicketTester().newFormTester(formPath());
		// Necessary because the submission button is outside the form
		Component submitButton = getWicketTester().getComponentFromLastRenderedPage(modalPath() + ":footer:save");
		String newPassword = "newPassword";
		form.setValue(form.getForm().get("oldPassword"), password);
		form.setValue(form.getForm().get("newPassword"), newPassword);
		form.setValue(form.getForm().get("confirmPassword"), newPassword);
		form.submit(submitButton);
		
		getWicketTester().assertNoErrorMessage();
		getWicketTester().assertFeedbackMessages(new ExactLevelFeedbackMessageFilter(FeedbackMessage.SUCCESS), localize("common.success"));
		
		assertTrue(passwordEncoder.matches(newPassword, userService.getByUsername(username).getPasswordHash()));
	}

	private String modalPath() {
		return modalPath("description:passwordEditPopup");
	}

	private String formPath() {
		return modalFormPath("description:passwordEditPopup");
	}
}
