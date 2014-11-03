package fr.openwide.core.basicapp.web.application.security.login.template;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fr.openwide.core.basicapp.core.business.parameter.service.IParameterService;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.web.application.common.template.ServiceTemplate;
import fr.openwide.core.basicapp.web.application.security.login.util.SignInUserTypeDescriptor;
import fr.openwide.core.jpa.security.service.IAuthenticationService;
import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SignInTemplate<U extends User> extends ServiceTemplate {

	private static final long serialVersionUID = 5503959273448832421L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SignInTemplate.class);

	@SpringBean
	private IParameterService parameterService;

	@SpringBean
	private IAuthenticationService authenticationService;

	@SpringBean
	private IUserService userService;

	private FormComponent<String> userNameField;

	private FormComponent<String> passwordField;
	
	private final SignInUserTypeDescriptor<U> typeDescriptor;

	public SignInTemplate(PageParameters parameters, SignInUserTypeDescriptor<U> typeDescriptor) {
		super(parameters);
		this.typeDescriptor = typeDescriptor;
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("signIn.pageTitle")));
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("signIn.welcomeText");
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		DelegatedMarkupPanel content = new DelegatedMarkupPanel(wicketId, "contentFragment", getClass());
		
		Form<Void> signInForm = new Form<Void>("signInForm") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				AbstractCoreSession<?> session = AbstractCoreSession.get();
				boolean success = false;
				try {
					session.signIn(userNameField.getModelObject(), passwordField.getModelObject());
					userService.signIn((User) session.getUser());
					success = true;
				} catch (BadCredentialsException e) {
					session.error(getString("signIn.error.authentication"));
				} catch (UsernameNotFoundException e) {
					session.error(getString("signIn.error.authentication"));
				} catch (DisabledException e) {
					session.error(getString("signIn.error.userDisabled"));
				} catch (Exception e) {
					LOGGER.error("Erreur inconnue lors de l'authentification de l'utilisateur", e);
					session.error(getString("signIn.error.unknown"));
				}
				
				if (success) {
					throw typeDescriptor.loginSuccessPageLinkDescriptor().newRestartResponseException();
				} else {
					throw typeDescriptor.signInPageLinkDescriptor().newRestartResponseException();
				}
			}
		};
		content.add(signInForm);
		
		userNameField = new RequiredTextField<String>("userName", Model.of(""));
		userNameField.setLabel(new ResourceModel("signIn.userName"));
		userNameField.add(new LabelPlaceholderBehavior());
		userNameField.setOutputMarkupId(true);
		signInForm.add(userNameField);
		
		passwordField = new PasswordTextField("password", Model.of("")).setRequired(true);
		passwordField.setLabel(new ResourceModel("signIn.password"));
		passwordField.add(new LabelPlaceholderBehavior());
		signInForm.add(passwordField);
		
		return content;
	}

	@Override
	protected Component getFooterComponent(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, "footerFragment", getClass());
		
		footer.add(
				typeDescriptor.passwordRecoveryPageLinkDescriptor().link("passwordRecovery")
		);
		
		return footer;
	}

}
