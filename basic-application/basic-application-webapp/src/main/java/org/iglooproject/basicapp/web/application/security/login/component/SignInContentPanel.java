package org.iglooproject.basicapp.web.application.security.login.component;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;

public class SignInContentPanel<U extends User> extends Panel {

	private static final long serialVersionUID = 5503959273448832421L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SignInContentPanel.class);

	@SpringBean
	private IUserService userService;

	private FormComponent<String> userNameField;

	private FormComponent<String> passwordField;
	
	public SignInContentPanel(String wicketId, final UserTypeDescriptor<U> defaultTypeDescriptor) {
		super(wicketId);
		
		Form<Void> signInForm = new Form<Void>("signInForm") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				AbstractCoreSession<?> session = AbstractCoreSession.get();
				User loggedInUser = null;
				boolean success = false;
				boolean badCredentials = false;
				try {
					session.signIn(userNameField.getModelObject(), passwordField.getModelObject());
					loggedInUser = (User) session.getUser();
					userService.onSignIn(loggedInUser);
					success = true;
				} catch (BadCredentialsException e) { // NOSONAR
					badCredentials = true;
					session.error(getString("signIn.error.authentication"));
				} catch (UsernameNotFoundException e) { // NOSONAR
					session.error(getString("signIn.error.authentication"));
				} catch (DisabledException e) { // NOSONAR
					session.error(getString("signIn.error.userDisabled"));
				} catch (Exception e) {
					LOGGER.error("Unknown error during authentification", e);
					session.error(getString("signIn.error.unknown"));
				}
				
				if (success) {
					/* Redirect the user depending on its type, and not based on the authentication page.
					 * This allows user to authenticate from the wrong page, when there's multiple authentication pages.
					 */
					throw UserTypeDescriptor.get(loggedInUser).securityTypeDescriptor().loginSuccessPageLinkDescriptor().newRestartResponseException();
				} else if (badCredentials) {
					User user = userService.getByUserName(userNameField.getModelObject());
					if (user != null) {
						try {
							userService.onSignInFail(user);
						} catch (Exception e1) {
							LOGGER.error("Unknown error while trying to find the user associated with the username entered in the form", e1);
							session.error(getString("signIn.error.unknown"));
						}
					}
				}
				throw defaultTypeDescriptor.securityTypeDescriptor().signInPageLinkDescriptor().newRestartResponseException();
			}
		};
		add(signInForm);
		
		userNameField = new RequiredTextField<String>("userName", Model.of(""));
		userNameField.setLabel(new ResourceModel("signIn.userName"));
		userNameField.add(new LabelPlaceholderBehavior());
		userNameField.setOutputMarkupId(true);
		signInForm.add(userNameField);
		
		passwordField = new PasswordTextField("password", Model.of("")).setRequired(true);
		passwordField.setLabel(new ResourceModel("signIn.password"));
		passwordField.add(new LabelPlaceholderBehavior());
		signInForm.add(passwordField);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		// There's javascript directly in the HTML file
		response.render(JavaScriptHeaderItem.forReference(
				Application.get().getJavaScriptLibrarySettings().getJQueryReference()
		));
	}

}
