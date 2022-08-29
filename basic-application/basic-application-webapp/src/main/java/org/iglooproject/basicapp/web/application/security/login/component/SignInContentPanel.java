package org.iglooproject.basicapp.web.application.security.login.component;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import igloo.igloojs.showpassword.ShowPasswordBehavior;

public class SignInContentPanel extends Panel {

	private static final long serialVersionUID = 5503959273448832421L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SignInContentPanel.class);

	@SpringBean
	private IUserService userService;

	public SignInContentPanel(String wicketId) {
		super(wicketId);
		
		IModel<String> usernameModel = Model.of();
		IModel<String> passwordModel = Model.of();
		
		Form<Void> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit() {
				try {
					BasicApplicationSession.get().signIn(usernameModel.getObject(), passwordModel.getObject());
					onSuccess(BasicApplicationSession.get().getUser());
					throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
				} catch (BadCredentialsException e) { // NOSONAR
					Session.get().error(getString("signIn.error.authentication"));
					onBadCredentials(usernameModel.getObject());
				} catch (UsernameNotFoundException | AccountStatusException e) { // NOSONAR
					Session.get().error(getString("signIn.error.authentication"));
				} catch (RestartResponseException e) { // NOSONAR
					throw e;
				} catch (Exception e) {
					LOGGER.error("Unknown error during authentification", e);
					Session.get().error(getString("signIn.error.authentication"));
				}
				
				throw BasicApplicationApplication.get().getSignInPageLinkDescriptor().newRestartResponseException();
			}
		};
		add(form);
		
		PasswordTextField password = new PasswordTextField("password", passwordModel);
		
		form
			.add(
				new TextField<>("username", usernameModel)
					.setRequired(true)
					.setLabel(new ResourceModel("signIn.username"))
					.add(new LabelPlaceholderBehavior())
					.setOutputMarkupId(true),
				password
					.setRequired(true)
					.setLabel(new ResourceModel("signIn.password"))
					.add(new LabelPlaceholderBehavior())
					.setOutputMarkupId(true),
				new BlankLink("showPassword")
					.add(new ShowPasswordBehavior(password))
			);
	}

	private void onSuccess(User user) throws ServiceException, SecurityServiceException {
		userService.onSignIn(user);
	}

	private void onBadCredentials(String username) {
		User user = userService.getByUsername(username);
		if (user != null) {
			try {
				userService.onSignInFail(user);
			} catch (Exception e) {
				LOGGER.error("Unknown error while trying to find the user associated with the username entered in the form", e);
			}
		}
	}

}
