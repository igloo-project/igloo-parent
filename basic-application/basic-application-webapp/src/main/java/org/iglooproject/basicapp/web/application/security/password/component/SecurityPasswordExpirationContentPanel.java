package org.iglooproject.basicapp.web.application.security.password.component;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.basicapp.web.application.common.validator.UserPasswordValidator;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;

public class SecurityPasswordExpirationContentPanel extends GenericPanel<User> {

	private static final long serialVersionUID = 547223775134254240L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPasswordExpirationContentPanel.class);
	
	private final IModel<String> newPasswordModel = Model.of("");

	@SpringBean
	private ISecurityManagementService securityManagementService;
	
	public SecurityPasswordExpirationContentPanel(String id) {
		super(id, BasicApplicationSession.get().getUserModel());
		
		UserTypeDescriptor<?> typeDescriptor = UserTypeDescriptor.get(getModelObject());
		
		Form<?> form = new Form<Void>("form");
		TextField<String> newPasswordField = new PasswordTextField("newPassword", newPasswordModel);
		TextField<String> confirmPasswordField = new PasswordTextField("confirmPassword", Model.of(""));
		
		add(form);
		form.add(
				newPasswordField
						.setLabel(new ResourceModel("business.user.newPassword"))
						.setRequired(true)
						.add(
								new UserPasswordValidator(typeDescriptor)
										.userModel(getModel())
						)
						.add(new LabelPlaceholderBehavior()),
				new CoreLabel("passwordHelp",
						new ResourceModel(
								typeDescriptor.securityTypeDescriptor().resourceKeyGenerator().resourceKey("password.help"),
								new ResourceModel(UserTypeDescriptor.USER.securityTypeDescriptor()
										.resourceKeyGenerator().resourceKey("password.help"))
						)
				),
				confirmPasswordField
						.setLabel(new ResourceModel("business.user.confirmPassword"))
						.setRequired(true)
						.add(new LabelPlaceholderBehavior()),
				new AjaxButton("validate", form) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						try {
							User user = BasicApplicationSession.get().getUser();
							securityManagementService.updatePassword(user, newPasswordModel.getObject());
							
							getSession().success(getString("security.password.expiration.validate.success"));
							
							throw UserTypeDescriptor.get(user).securityTypeDescriptor()
									.loginSuccessPageLinkDescriptor().newRestartResponseException();
						} catch (RestartResponseException e) {
							throw e;
						} catch (Exception e) {
							LOGGER.error("Error occurred while reseting password after expiration", e);
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
		newPasswordModel.detach();
	}

}
