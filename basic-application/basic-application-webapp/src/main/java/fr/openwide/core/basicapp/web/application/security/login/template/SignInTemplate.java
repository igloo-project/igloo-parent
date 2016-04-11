package fr.openwide.core.basicapp.web.application.security.login.template;

import static fr.openwide.core.commons.util.functional.Predicates2.isTrue;
import static fr.openwide.core.wicket.more.condition.Condition.predicate;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Fragment;
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

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.web.application.common.template.ApplicationAccessTemplate;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SignInTemplate<U extends User> extends ApplicationAccessTemplate {

	private static final long serialVersionUID = 5503959273448832421L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SignInTemplate.class);

	@SpringBean
	private IUserService userService;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	private FormComponent<String> userNameField;

	private FormComponent<String> passwordField;
	
	private final UserTypeDescriptor<U> typeDescriptor;

	public SignInTemplate(PageParameters parameters, UserTypeDescriptor<U> typeDescriptor) {
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
		Fragment content = new Fragment(wicketId, "contentFragment", this);
		
		Form<Void> signInForm = new Form<Void>("signInForm") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				AbstractCoreSession<?> session = AbstractCoreSession.get();
				boolean success = false;
				boolean badCredentials = false;
				try {
					session.signIn(userNameField.getModelObject(), passwordField.getModelObject());
					userService.onSignIn((User) session.getUser());
					success = true;
				} catch (BadCredentialsException e) {
					badCredentials = true;
					session.error(getString("signIn.error.authentication"));
				} catch (UsernameNotFoundException e) {
					session.error(getString("signIn.error.authentication"));
				} catch (DisabledException e) {
					session.error(getString("signIn.error.userDisabled"));
				} catch (Exception e) {
					LOGGER.error("Unknown error during authentification", e);
					session.error(getString("signIn.error.unknown"));
				}
				
				if (success) {
					throw typeDescriptor.securityTypeDescriptor().loginSuccessPageLinkDescriptor().newRestartResponseException();
				} else if (badCredentials) {
					User user = userService.getByUserName(userNameField.getModelObject());
					if (user != null) {
						try {
							userService.onSignInFail(user);
						}
						catch (Exception e1) {
							LOGGER.error("Unknown error while trying to find the user associated with the username entered in the form", e1);
							session.error(getString("signIn.error.unknown"));
						}
					}
				}
				throw typeDescriptor.securityTypeDescriptor().signInPageLinkDescriptor().newRestartResponseException();
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
		Fragment footer = new Fragment(wicketId, "footerFragment", this);
		
		footer.add(
				typeDescriptor.securityTypeDescriptor().passwordRecoveryPageLinkDescriptor().link("passwordRecovery")
						.add(new EnclosureBehavior()
								.condition(predicate(
										Model.of(securityManagementService.getOptions(typeDescriptor.getEntityClass()).isPasswordUserRecoveryEnabled()),
										isTrue()
								))
						)
		);
		
		return footer;
	}

}
