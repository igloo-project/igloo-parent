package org.iglooproject.basicapp.web.application.security.password.component;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.typedescriptor.UserTypeDescriptor;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.common.model.UserTypeDescriptorModel;
import org.iglooproject.basicapp.web.application.common.validator.UserPasswordValidator;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.form.ModelValidatingForm;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityPasswordExpirationContentPanel extends GenericPanel<User> {

	private static final long serialVersionUID = 547223775134254240L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPasswordExpirationContentPanel.class);

	@SpringBean
	private ISecurityManagementService securityManagementService;

	private final IModel<? extends UserTypeDescriptor<? extends User>> userTypeDescriptorModel;

	private final IModel<String> newPasswordModel = Model.of();

	public SecurityPasswordExpirationContentPanel(String id) {
		super(id, BasicApplicationSession.get().getUserModel());
		
		userTypeDescriptorModel = UserTypeDescriptorModel.fromUser(getModel());
		
		ModelValidatingForm<?> form = new ModelValidatingForm<>("form");
		add(form);
		
		TextField<String> newPasswordField = new PasswordTextField("newPassword", newPasswordModel);
		TextField<String> confirmPasswordField = new PasswordTextField("confirmPassword", Model.of());
		
		form.add(
			newPasswordField
				.setLabel(new ResourceModel("business.user.newPassword"))
				.setRequired(true)
				.add(new LabelPlaceholderBehavior()),
			new CoreLabel("passwordHelp",
				new StringResourceModel("security.${resourceKeyBase}.password.help", userTypeDescriptorModel)
					.setDefaultValue(new ResourceModel("security.user.password.help"))
			),
			confirmPasswordField
				.setLabel(new ResourceModel("business.user.confirmPassword"))
				.setRequired(true)
				.add(new LabelPlaceholderBehavior())
		);
		
		form.add(new EqualPasswordInputValidator(newPasswordField, confirmPasswordField));
		
		form.add(
			new UserPasswordValidator(userTypeDescriptorModel.map(UserTypeDescriptor::getClazz), newPasswordField)
				.userModel(getModel())
		);
		
		form.add(
			new AjaxButton("validate", form) {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					try {
						User user = BasicApplicationSession.get().getUser();
						securityManagementService.updatePassword(user, newPasswordModel.getObject());
						
						Session.get().success(getString("security.password.expiration.validate.success"));
						
						throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
					} catch (RestartResponseException e) {
						throw e;
					} catch (Exception e) {
						LOGGER.error("Error occurred while reseting password after expiration", e);
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
			newPasswordModel
		);
	}

}
