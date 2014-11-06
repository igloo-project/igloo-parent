package fr.openwide.core.basicapp.web.application.security.password.page;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.validator.EmailExistsValidator;
import fr.openwide.core.basicapp.web.application.common.validator.UserPasswordValidator;
import fr.openwide.core.basicapp.web.application.navigation.link.LinkUtils;
import fr.openwide.core.basicapp.web.application.security.password.template.SecurityPasswordTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class SecurityPasswordResetPage extends SecurityPasswordTemplate {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPasswordResetPage.class);

	public static IPageLinkDescriptor linkDescriptor(IModel<User> userModel, IModel<String> tokenModel) {
		return new LinkDescriptorBuilder()
				.page(SecurityPasswordResetPage.class)
				.map(CommonParameters.ID, userModel, User.class).mandatory()
				.map(LinkUtils.TOKEN, tokenModel, String.class).mandatory()
				.build();
	}
	
	@SpringBean
	private IUserService userService;

	private final IModel<User> userModel = new GenericEntityModel<Long, User>();

	private final IModel<String> tokenModel = Model.of("");

	private final IModel<String> emailModel = Model.of("");

	private final IModel<String> newPasswordModel = Model.of("");

	@SpringBean
	private ISecurityManagementService securityManagementService;

	public SecurityPasswordResetPage(PageParameters parameters) {
		super(parameters);
		
		linkDescriptor(userModel, tokenModel).extractSafely(parameters, BasicApplicationApplication.get().getHomePageLinkDescriptor(), getString("common.error.unexpected"));
		
		if (!tokenModel.getObject().equals(userModel.getObject().getPasswordRecoveryRequest().getToken())) {
			getSession().error(getString("security.password.reset.wrongToken"));
			throw BasicApplicationApplication.get().getHomePageLinkDescriptor().newRestartResponseException();
		}
		
		if (securityManagementService.isPasswordRecoveryRequestExpired(userModel.getObject())) {
			getSession().error(getString("security.password.reset.expired"));
			throw BasicApplicationApplication.get().getHomePageLinkDescriptor().newRestartResponseException();
		}
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("security.password.reset.pageTitle")));
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("security.password.reset.pageTitle");
	}

	@Override
	protected Component getIntroComponent(String wicketId) {
		return new Fragment(wicketId, "introFragment", this);
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		Fragment content = new Fragment(wicketId, "contentFragment", this);
		
		Form<?> form = new Form<Void>("form");
		TextField<String> newPasswordField = new PasswordTextField("newPassword", newPasswordModel);
		TextField<String> confirmPasswordField = new PasswordTextField("confirmPassword", Model.of(""));
		
		content.add(
				form
						.add(
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
											User user = userModel.getObject();
											securityManagementService.updatePassword(user, newPasswordModel.getObject());
											
											getSession().success(getString("security.password.reset.validate.success"));
											
											throw UserTypeDescriptor.get(user).securityTypeDescriptor().loginSuccessPageLinkDescriptor().newRestartResponseException();
										} catch (RestartResponseException e) {
											throw e;
										} catch (Exception e) {
											LOGGER.error("Error occurred while password reset", e);
											getSession().error(getString("common.error.unexpected"));
										}
										
										FeedbackUtils.refreshFeedback(target, getPage());
									};
									
									@Override
									protected void onError(AjaxRequestTarget target, Form<?> form) {
										FeedbackUtils.refreshFeedback(target, getPage());
									}
								}
						)
						.add(new EqualPasswordInputValidator(newPasswordField, confirmPasswordField))
		);
		
		return content;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		userModel.detach();
		tokenModel.detach();
		emailModel.detach();
		newPasswordModel.detach();
	}

}
