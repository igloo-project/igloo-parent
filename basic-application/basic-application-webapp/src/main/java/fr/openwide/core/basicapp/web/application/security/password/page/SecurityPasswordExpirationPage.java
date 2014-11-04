package fr.openwide.core.basicapp.web.application.security.password.page;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.security.service.ISecurityOptionsService;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.security.password.template.SecurityPasswordTemplate;
import fr.openwide.core.basicapp.web.application.security.util.SecurityUserTypeDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
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
	private IUserService userService;

	private Form<?> passwordForm;

	private TextField<String> newPasswordField;

	private TextField<String> confirmPasswordField;

	@SpringBean
	private ISecurityOptionsService securityOptionsService;

	public SecurityPasswordExpirationPage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("security.password.expiration.pageTitle")));
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("security.password.expiration.pageTitle");
	}

	@Override
	protected Component getIntroComponent(String wicketId) {
		return new DelegatedMarkupPanel(wicketId, "introFragment", getClass());
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		DelegatedMarkupPanel content = new DelegatedMarkupPanel(wicketId, "contentFragment", getClass());
		
		passwordForm = new Form<Void>("form");
		content.add(passwordForm);
		
		newPasswordField = new PasswordTextField("newPassword", Model.of(""));
		newPasswordField.setLabel(new ResourceModel("business.user.newPassword"));
		newPasswordField.setRequired(true);
		newPasswordField.add(new LabelPlaceholderBehavior());
		passwordForm.add(newPasswordField);
		
		confirmPasswordField = new PasswordTextField("confirmPassword", Model.of(""));
		confirmPasswordField.setLabel(new ResourceModel("business.user.confirmPassword"));
		confirmPasswordField.setRequired(true);
		confirmPasswordField.add(new LabelPlaceholderBehavior());
		passwordForm.add(confirmPasswordField);
		
		AjaxButton validationButton = new AjaxButton("validate", passwordForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {
					User user = BasicApplicationSession.get().getUser();
					
					if (user == null) {
						getSession().error(getString("security.password.expiration.validate.error"));
						FeedbackUtils.refreshFeedback(target, getPage());
						return;
					}
					
					String newPasswordValue = newPasswordField.getModelObject();
					String confirmPasswordValue = confirmPasswordField.getModelObject();
					
					if (newPasswordValue != null && confirmPasswordValue != null) {
						if (confirmPasswordValue.equals(newPasswordValue)) {
							if (newPasswordValue.length() >= User.MIN_PASSWORD_LENGTH && 
									newPasswordValue.length() <= User.MAX_PASSWORD_LENGTH) {
								userService.updatePassword(user, newPasswordValue);
								
								getSession().success(getString("security.password.expiration.validate.success"));
							} else {
								getSession().error(getString("security.password.expiration.validate.malformed"));
								FeedbackUtils.refreshFeedback(target, getPage());
								return;
							}
						} else {
							getSession().error(getString("security.password.expiration.validate.wrongConfirmation"));
							FeedbackUtils.refreshFeedback(target, getPage());
							return;
						}
					}
					
					throw SecurityUserTypeDescriptor.<SecurityUserTypeDescriptor<User>, User>get(user).loginSuccessPageLinkDescriptor().newRestartResponseException();
				} catch (RestartResponseException e) {
					throw e;
				} catch (Exception e) {
					LOGGER.error("Error occurred while password reset", e);
					getSession().error(getString("common.error.unexpected"));
				}
				
				FeedbackUtils.refreshFeedback(target, getPage());
			};
		};
		
		passwordForm.add(validationButton);
		
		return content;
	}

}
