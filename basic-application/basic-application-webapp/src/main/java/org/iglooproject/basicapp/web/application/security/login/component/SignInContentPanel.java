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
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.application.CoreWicketAuthenticatedApplication;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SignInContentPanel extends Panel {

	private static final long serialVersionUID = 5503959273448832421L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SignInContentPanel.class);

	@SpringBean
	private IUserService userService;

	private FormComponent<String> usernameField;

	private FormComponent<String> passwordField;
	
	public SignInContentPanel(String wicketId) {
		super(wicketId);
		
		Form<Void> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				AbstractCoreSession<?> session = AbstractCoreSession.get();
				User loggedInUser = null;
				boolean success = false;
				boolean badCredentials = false;
				try {
					session.signIn(usernameField.getModelObject(), passwordField.getModelObject());
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
					throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
				} else if (badCredentials) {
					User user = userService.getByUsername(usernameField.getModelObject());
					if (user != null) {
						try {
							userService.onSignInFail(user);
						} catch (Exception e1) {
							LOGGER.error("Unknown error while trying to find the user associated with the username entered in the form", e1);
							session.error(getString("signIn.error.unknown"));
						}
					}
				}
				
				throw CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor().newRestartResponseException();
			}
		};
		add(form);
		
		usernameField = new RequiredTextField<>("username", Model.of(""));
		usernameField.setLabel(new ResourceModel("signIn.username"));
		usernameField.add(new LabelPlaceholderBehavior());
		usernameField.setOutputMarkupId(true);
		form.add(usernameField);
		
		passwordField = new PasswordTextField("password", Model.of("")).setRequired(true);
		passwordField.setLabel(new ResourceModel("signIn.password"));
		passwordField.add(new LabelPlaceholderBehavior());
		form.add(passwordField);
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
