package basicapp.front.administration.form;

import static basicapp.back.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_LENGTH_MIN;

import org.apache.wicket.Component;
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
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.markup.html.form.ModelValidatingForm;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import basicapp.back.business.user.model.TechnicalUser;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.IUserService;
import basicapp.back.business.user.typedescriptor.UserTypeDescriptor;
import basicapp.back.security.service.IBasicApplicationAuthenticationService;
import basicapp.back.security.service.ISecurityManagementService;
import basicapp.front.BasicApplicationSession;
import basicapp.front.common.model.UserTypeDescriptorModel;
import basicapp.front.common.validator.UserPasswordValidator;
import igloo.bootstrap.modal.AbstractAjaxModalPopupPanel;
import igloo.igloojs.showpassword.ShowPasswordBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.Detachables;

public class UserPasswordEditPopup<U extends User> extends AbstractAjaxModalPopupPanel<U> {

	private static final long serialVersionUID = -4580284817084080271L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPasswordEditPopup.class);

	@SpringBean
	private IUserService userService;

	@SpringBean
	private IBasicApplicationAuthenticationService authenticationService;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	private final IModel<? extends UserTypeDescriptor<? extends User>> userTypeDescriptorModel;

	private ModelValidatingForm<?> form;

	private final IModel<String> oldPasswordModel = Model.of("");
	private final IModel<String> newPasswordModel = Model.of("");

	private final Condition isOldPasswordRequired;

	public UserPasswordEditPopup(String id, IModel<U> userModel) {
		super(id, userModel);
		
		this.userTypeDescriptorModel = UserTypeDescriptorModel.fromUser(getModel());
		
		this.isOldPasswordRequired = Condition.or(
			Condition.isEqual(userModel.map(User::getClass), Model.of(TechnicalUser.class)),
			Condition.role(CoreAuthorityConstants.ROLE_ADMIN)
		)
			.negate();
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new CoreLabel(wicketId, new ResourceModel("administration.user.action.password.edit.title"));
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, UserPasswordEditPopup.class);
		
		form = new ModelValidatingForm<>("form");
		body.add(form);
		
		TextField<String> oldPassword = new PasswordTextField("oldPassword", oldPasswordModel);
		TextField<String> newPassword = new PasswordTextField("newPassword", newPasswordModel);
		
		form
			.add(
				new EnclosureContainer("oldPasswordContainer")
					.condition(isOldPasswordRequired)
					.add(
						oldPassword
							.setLabel(new ResourceModel("business.user.oldPassword"))
							.setRequired(true),
						new BlankLink("showOldPassword")
							.add(new ShowPasswordBehavior(oldPassword))
					),
				newPassword
					.setLabel(new ResourceModel("business.user.newPassword"))
					.setRequired(true),
				new BlankLink("showNewPassword")
					.add(new ShowPasswordBehavior(newPassword)),
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
			new UserPasswordValidator(userTypeDescriptorModel.map(UserTypeDescriptor::getClazz), newPassword)
				.userModel(getModel())
		);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, UserPasswordEditPopup.class);
		
		footer.add(
			new AjaxButton("save", form) {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					try {
						IModel<U> userModel = UserPasswordEditPopup.this.getModel();
						U user = userModel.getObject();
						
						String oldPassword = oldPasswordModel.getObject();
						String newPassword = newPasswordModel.getObject();
						
						if (!isOldPasswordRequired.applies() || securityManagementService.checkPassword(oldPassword, user)) {
							securityManagementService.updatePassword(user, newPassword, BasicApplicationSession.get().getUser());
							
							Session.get().success(getString("common.success"));
							closePopup(target);
							target.add(getPage());
						} else {
							Session.get().error(getString("administration.user.action.password.edit.error.oldPassword"));
						}
					} catch (Exception e) {
						LOGGER.error("Error occured while changing password.", e);
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
		
		BlankLink cancel = new BlankLink("cancel");
		addCancelBehavior(cancel);
		footer.add(cancel);
		
		return footer;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(
			userTypeDescriptorModel,
			oldPasswordModel,
			newPasswordModel,
			isOldPasswordRequired
		);
	}

}
