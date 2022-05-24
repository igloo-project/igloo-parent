package org.iglooproject.wicket.bootstrap5.console.navigation.page;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.bootstrap5.console.template.ConsoleAccessTemplate;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ConsoleSignInPage extends ConsoleAccessTemplate {

	private static final long serialVersionUID = 3401416708867386953L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleSignInPage.class);

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(ConsoleSignInPage.class);
	}
	
	public ConsoleSignInPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("console.signIn.welcomeText");
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		return new ContentFragment(wicketId);
	}

	private class ContentFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public ContentFragment(String id) {
			super(id, "content", ConsoleSignInPage.this);
			
			FormComponent<String> usernameField = new RequiredTextField<>("username", Model.of(""));
			FormComponent<String> passwordField = new PasswordTextField("password", Model.of(""));
			
			Form<Void> form = new Form<Void>("form") {
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
			add(form);
			
			usernameField.setLabel(new ResourceModel("console.signIn.username"));
			usernameField.add(new LabelPlaceholderBehavior());
			usernameField.setOutputMarkupId(true);
			form.add(usernameField);
			
			passwordField.setRequired(true);
			passwordField.setLabel(new ResourceModel("console.signIn.password"));
			passwordField.add(new LabelPlaceholderBehavior());
			form.add(passwordField);
		}
		
	}

}
