package fr.openwide.core.basicapp.web.application.security.password.page;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.validator.UserPasswordValidator;
import fr.openwide.core.basicapp.web.application.security.password.template.SecurityPasswordTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SecurityPasswordExpirationPage extends SecurityPasswordTemplate {

	private static final long serialVersionUID = 547223775134254240L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPasswordExpirationPage.class);

	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(SecurityPasswordExpirationPage.class)
				.build();
	}

	@SpringBean
	private ISecurityManagementService securityManagementService;

	private final IModel<String> newPasswordModel = Model.of("");

	public SecurityPasswordExpirationPage(PageParameters parameters) {
		super(parameters);
		
		// Ca n'a pas de sens d'être connecté sur cette page.
		BasicApplicationSession.get().signOut();
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("security.password.expiration.pageTitle")));
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("security.password.expiration.pageTitle");
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
								newPasswordField
										.setLabel(new ResourceModel("business.user.newPassword"))
										.setRequired(true)
										.add(
												new UserPasswordValidator(UserTypeDescriptor.get(BasicApplicationSession.get().getUser()))
														.userModel(BasicApplicationSession.get().getUserModel())
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
											User user = BasicApplicationSession.get().getUser();
											securityManagementService.updatePassword(user, newPasswordModel.getObject());
											
											getSession().success(getString("security.password.expiration.validate.success"));
											
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
		newPasswordModel.detach();
	}

}
