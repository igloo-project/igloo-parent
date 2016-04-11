package fr.openwide.core.basicapp.web.application.security.password.component;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.validator.EmailExistsValidator;
import fr.openwide.core.basicapp.web.application.common.validator.UserPasswordValidator;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;

public class SecurityPasswordCreationContentPanel extends GenericPanel<User> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPasswordCreationContentPanel.class);

	private final IModel<String> emailModel = Model.of("");

	private final IModel<String> newPasswordModel = Model.of("");

	@SpringBean
	private ISecurityManagementService securityManagementService;

	public SecurityPasswordCreationContentPanel(String wicketId, IModel<User> userModel) {
		super(wicketId, userModel);
		
		Form<?> form = new Form<Void>("form");
		TextField<String> newPasswordField = new PasswordTextField("newPassword", newPasswordModel);
		TextField<String> confirmPasswordField = new PasswordTextField("confirmPassword", Model.of(""));
		
		add(form);
		form.add(
				new RequiredTextField<String>("email", emailModel)
						.setLabel(new ResourceModel("business.user.email"))
						.add(EmailAddressValidator.getInstance())
						.add(EmailExistsValidator.get())
						.add(new LabelPlaceholderBehavior()),
				newPasswordField
						.setLabel(new ResourceModel("business.user.newPassword"))
						.setRequired(true)
						.add(
								new UserPasswordValidator(UserTypeDescriptor.get(userModel.getObject()))
										.userModel(userModel)
						)
						.add(new LabelPlaceholderBehavior()),
				confirmPasswordField
						.setLabel(new ResourceModel("business.user.confirmPassword"))
						.setRequired(true)
						.add(new LabelPlaceholderBehavior()),
				new AjaxButton("validate", form) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						try {
							User user = SecurityPasswordCreationContentPanel.this.getModelObject();
							securityManagementService.updatePassword(user, newPasswordModel.getObject());
							
							getSession().success(getString("security.password.creation.validate.success"));
							
							throw UserTypeDescriptor.get(user).securityTypeDescriptor()
									.loginSuccessPageLinkDescriptor().newRestartResponseException();
						} catch (RestartResponseException e) {
							throw e;
						} catch (Exception e) {
							LOGGER.error("Error occurred while creating password", e);
							getSession().error(getString("common.error.unexpected"));
						}
						
						FeedbackUtils.refreshFeedback(target, getPage());
					}
					
					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form) {
						FeedbackUtils.refreshFeedback(target, getPage());
					}
				}
		);
		form.add(new EqualPasswordInputValidator(newPasswordField, confirmPasswordField));
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		emailModel.detach();
		newPasswordModel.detach();
	}

}
