package fr.openwide.core.showcase.web.application.navigation.page;

import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.odlabs.wiquery.core.IWiQueryPlugin;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fr.openwide.core.showcase.web.application.ShowcaseSession;
import fr.openwide.core.showcase.web.application.util.template.styles.SignInLessCssResourceReference;
import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;
import fr.openwide.core.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;

public class SignInPage extends CoreWebPage implements IWiQueryPlugin {
	private static final long serialVersionUID = 5503959273448832421L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SignInPage.class);

	private FormComponent<String> userNameField;
	
	private FormComponent<String> passwordField;
	
	public SignInPage() {
		super();
		
		add(new AnimatedGlobalFeedbackPanel("feedback"));
		
		Form<Void> signInForm = new Form<Void>("signInForm") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				ShowcaseSession showcaseSession = ShowcaseSession.get();
				try {
					showcaseSession.signIn(userNameField.getModelObject(), passwordField.getModelObject());
					showcaseSession.success(getString("signIn.success"));
				} catch (BadCredentialsException e) {
					showcaseSession.error(getString("signIn.error.authentication"));
				} catch (UsernameNotFoundException e) {
					showcaseSession.error(getString("signIn.error.authentication"));
				} catch (DisabledException e) {
					showcaseSession.error(getString("signIn.error.userDisabled"));
				} catch (Exception e) {
					LOGGER.error("Erreur inconnue lors de l'authentification de l'utilisateur", e);
					showcaseSession.error(getString("signIn.error.unknown"));
				}
				
				if (StringUtils.hasText(showcaseSession.getRedirectUrl())) {
					throw new RedirectToUrlException(showcaseSession.getRedirectUrl());
				} else {
					throw new RestartResponseException(Application.get().getHomePage());
				}
			}
		};
		add(signInForm);
		
		userNameField = new RequiredTextField<String>("userName", Model.of(""));
		userNameField.setLabel(new ResourceModel("signIn.login"));
		userNameField.setOutputMarkupId(true);
		signInForm.add(userNameField);
		
		passwordField = new PasswordTextField("password", Model.of("")).setRequired(true);
		passwordField.setLabel(new ResourceModel("signIn.password"));
		signInForm.add(passwordField);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
		response.renderCSSReference(SignInLessCssResourceReference.get());
	}
	
	@Override
	public JsStatement statement() {
		return null;
	}
}
