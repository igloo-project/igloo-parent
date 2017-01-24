package fr.openwide.core.basicapp.web.application.administration.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.model.TechnicalUser;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.security.model.BasicApplicationAuthorityConstants;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.validator.UserPasswordValidator;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;

public class UserPasswordUpdatePopup<U extends User> extends AbstractAjaxModalPopupPanel<U> {

	private static final long serialVersionUID = -4580284817084080271L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPasswordUpdatePopup.class);

	@SpringBean
	private IUserService userService;
	
	@SpringBean
	private ISecurityManagementService securityManagementService;

	private Form<?> passwordForm;

	private final IModel<String> oldPasswordModel = Model.of("");
	private final IModel<String> newPasswordModel = Model.of("");

	private final UserTypeDescriptor<U> typeDescriptor;

	private final Condition isOldPasswordRequired;
	
	public UserPasswordUpdatePopup(String id, IModel<U> model) {
		super(id, model);
		setStatic();
		
		this.typeDescriptor = UserTypeDescriptor.get(model.getObject());
		
		this.isOldPasswordRequired = new Condition() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean applies() {
				User user = userService.getAuthenticatedUser();
				if (BasicUser.class.equals(user.getClass())) {
					return user.getAuthorities().contains(BasicApplicationAuthorityConstants.ROLE_ADMIN);
				} else {
					return TechnicalUser.class.equals(user.getClass());
				}
			}
		}.negate();
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new Label(wicketId, new ResourceModel("administration.user.password.update.title"));
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, UserPasswordUpdatePopup.class);
		
		passwordForm = new Form<Void>("form");
		TextField<String> oldPasswordField = new PasswordTextField("oldPassword", oldPasswordModel);
		TextField<String> newPasswordField = new PasswordTextField("newPassword", newPasswordModel);
		TextField<String> confirmPasswordField = new PasswordTextField("confirmPassword", Model.of(""));
		
		body.add(
				passwordForm
						.add(
								oldPasswordField
										.setLabel(new ResourceModel("business.user.oldPassword"))
										.setRequired(true)
										.add(isOldPasswordRequired.thenShow()),
								newPasswordField
										.setLabel(new ResourceModel("business.user.newPassword"))
										.setRequired(true)
										.add(
												new UserPasswordValidator(typeDescriptor)
														.userModel(getModel())
										),
								
								new CoreLabel("passwordHelp",
										new ResourceModel(
												typeDescriptor.securityTypeDescriptor().resourceKeyGenerator().resourceKey("password.help"),
												new ResourceModel(UserTypeDescriptor.USER.securityTypeDescriptor().resourceKeyGenerator().resourceKey("password.help"))
										)
								),
								
								confirmPasswordField
										.setLabel(new ResourceModel("business.user.confirmPassword"))
										.setRequired(true)
						)
						.add(new EqualPasswordInputValidator(newPasswordField, confirmPasswordField))
		);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, UserPasswordUpdatePopup.class);
		
		// Validate button
		AjaxButton validate = new AjaxButton("save", passwordForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {
					User user = UserPasswordUpdatePopup.this.getModelObject();
					String oldPassword = oldPasswordModel.getObject();
					String newPassword = newPasswordModel.getObject();
					
					if (!isOldPasswordRequired.applies() || securityManagementService.checkPassword(oldPassword, user)) {
						securityManagementService.updatePassword(user, newPassword, BasicApplicationSession.get().getUser());
						
						getSession().success(getString("administration.user.password.update.success"));
						closePopup(target);
						target.add(getPage());
					} else {
						getSession().error(getString("administration.user.password.update.error.oldPassword"));
					}
				} catch (Exception e) {
					LOGGER.error("Error occured while changing password.", e);
					getSession().error(getString("common.error.unexpected"));
				}
				FeedbackUtils.refreshFeedback(target, getPage());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				FeedbackUtils.refreshFeedback(target, getPage());
			}
		};
		footer.add(validate);
		
		// Cancel button
		AbstractLink cancel = new AbstractLink("cancel") {
			private static final long serialVersionUID = 1L;
		};
		addCancelBehavior(cancel);
		footer.add(cancel);
		
		return footer;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		oldPasswordModel.detach();
		newPasswordModel.detach();
	}
}
