package org.iglooproject.basicapp.web.application.security.password.component;

import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_LENGTH_MIN;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.typedescriptor.UserTypeDescriptor;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.web.application.common.model.UserTypeDescriptorModel;
import org.iglooproject.basicapp.web.application.common.validator.UserPasswordValidator;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.form.ModelValidatingForm;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.iglooproject.wicket.more.util.validate.validators.PredicateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import igloo.igloojs.showpassword.ShowPasswordBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Detachables;

public class SecurityPasswordCreationContentPanel extends GenericPanel<User> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPasswordCreationContentPanel.class);

	@SpringBean
	private ISecurityManagementService securityManagementService;

	private final IModel<? extends UserTypeDescriptor<? extends User>> userTypeDescriptorModel;

	private final IModel<String> emailModel = Model.of("");

	private final IModel<String> passwordModel = Model.of("");

	public SecurityPasswordCreationContentPanel(String wicketId, IModel<User> userModel) {
		super(wicketId, userModel);
		
		userTypeDescriptorModel = UserTypeDescriptorModel.fromUser(getModel());
		
		ModelValidatingForm<?> form = new ModelValidatingForm<>("form");
		add(form);
		
		TextField<String> password = new PasswordTextField("password", passwordModel);
		
		form.add(
			new TextField<String>("email", emailModel)
				.setLabel(new ResourceModel("business.user.email"))
				.setRequired(true)
				.add(new LabelPlaceholderBehavior())
				.add(
					PredicateValidator.of(email -> email != null && email.equals(getModelObject().getEmail()))
						.errorKey("common.validator.email.match.user")
				)
				.setOutputMarkupId(true),
			password
				.setLabel(new ResourceModel("business.user.password"))
				.setRequired(true)
				.add(new LabelPlaceholderBehavior())
				.setOutputMarkupId(true),
			new BlankLink("showPassword")
				.add(new ShowPasswordBehavior(password)),
			new CoreLabel("passwordHelp",
				new StringResourceModel("security.${resourceKeyBase}.password.help", userTypeDescriptorModel)
					.setParameters(ApplicationPropertyModel.of(SECURITY_PASSWORD_LENGTH_MIN))
					.setDefaultValue(
						new StringResourceModel("security.user.password.help")
							.setParameters(ApplicationPropertyModel.of(SECURITY_PASSWORD_LENGTH_MIN))
					)
			)
		);
		
		form.add(
			new UserPasswordValidator(userTypeDescriptorModel.map(UserTypeDescriptor::getClazz), password)
				.userModel(getModel())
		);
		
		form.add(
			new AjaxButton("validate", form) {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					try {
						User user = SecurityPasswordCreationContentPanel.this.getModelObject();
						securityManagementService.updatePassword(user, passwordModel.getObject());
						
						Session.get().success(getString("security.password.creation.validate.success"));
						
						throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
					} catch (RestartResponseException e) {
						throw e;
					} catch (Exception e) {
						LOGGER.error("Error occurred while creating password", e);
						Session.get().error(getString("common.error.unexpected"));
					}
					
					FeedbackUtils.refreshFeedback(target, getPage());
				}
				
				@Override
				protected void onError(AjaxRequestTarget target) {
					FeedbackUtils.refreshFeedback(target, getPage());
				}
			}
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(
			userTypeDescriptorModel,
			emailModel,
			passwordModel
		);
	}

}
