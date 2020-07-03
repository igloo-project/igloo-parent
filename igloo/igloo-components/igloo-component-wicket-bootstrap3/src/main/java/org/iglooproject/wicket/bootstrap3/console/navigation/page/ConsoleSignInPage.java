package org.iglooproject.wicket.bootstrap3.console.navigation.page;

import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.bootstrap3.console.template.style.ConsoleSignInLessCssResourceReference;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.CoreWebPage;
import org.iglooproject.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ConsoleSignInPage extends CoreWebPage {

	private static final long serialVersionUID = 3401416708867386953L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleSignInPage.class);
	
	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(ConsoleSignInPage.class);
	}
	
	private FormComponent<String> usernameField;
	
	private FormComponent<String> passwordField;
	
	public ConsoleSignInPage() {
		super();
		
		add(new AnimatedGlobalFeedbackPanel("feedback"));
		
		Form<Void> signInForm = new Form<Void>("signInForm") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				AbstractCoreSession<?> session = AbstractCoreSession.get();
				boolean success = false;
				try {
					session.signIn(usernameField.getModelObject(), passwordField.getModelObject());
					success = true;
				} catch (BadCredentialsException e) {
					session.error(getString("console.signIn.error.authentication"));
				} catch (UsernameNotFoundException e) {
					session.error(getString("console.signIn.error.authentication"));
				} catch (DisabledException e) {
					session.error(getString("console.signIn.error.userDisabled"));
				} catch (Exception e) {
					LOGGER.error("Erreur inconnue lors de l'authentification de l'utilisateur", e);
					session.error(getString("console.signIn.error.unknown"));
				}
				
				if (success) {
					throw new RestartResponseException(ConsoleLoginSuccessPage.class);
				} else {
					throw new RestartResponseException(ConsoleLoginFailurePage.class);
				}
			}
		};
		add(signInForm);
		
		usernameField = new RequiredTextField<>("username", Model.of(""));
		usernameField.setLabel(new ResourceModel("console.signIn.username"));
		usernameField.add(new LabelPlaceholderBehavior());
		usernameField.setOutputMarkupId(true);
		signInForm.add(usernameField);
		
		passwordField = new PasswordTextField("password", Model.of("")).setRequired(true);
		passwordField.setLabel(new ResourceModel("console.signIn.password"));
		passwordField.add(new LabelPlaceholderBehavior());
		signInForm.add(passwordField);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
		response.render(CssHeaderItem.forReference(ConsoleSignInLessCssResourceReference.get()));
	}

	@Override
	public String getVariation() {
		return AbstractWebPageTemplate.BOOTSTRAP3_VARIATION;
	}

}
