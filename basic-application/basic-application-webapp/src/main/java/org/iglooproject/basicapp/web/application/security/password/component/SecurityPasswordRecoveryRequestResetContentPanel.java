package org.iglooproject.basicapp.web.application.security.password.component;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.wicket.more.application.CoreWicketAuthenticatedApplication;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityPasswordRecoveryRequestResetContentPanel extends Panel {

	private static final long serialVersionUID = 547223775134254240L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPasswordRecoveryRequestResetContentPanel.class);

	@SpringBean
	private IUserService userService;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	private final IModel<String> emailModel = Model.of();

	public SecurityPasswordRecoveryRequestResetContentPanel(String wicketId) {
		super(wicketId);
		
		Form<?> form = new Form<>("form");
		add(form);
		
		form.add(
			new TextField<>("email", emailModel)
				.setLabel(new ResourceModel("business.user.email"))
				.setRequired(true)
				.add(EmailAddressValidator.getInstance())
				.add(new LabelPlaceholderBehavior())
		);
		
		form.add(
			new AjaxButton("validate", form) {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					try {
						User user = userService.getByEmailCaseInsensitive(emailModel.getObject());
						
						if (user != null && user.isEnabled() && user.isNotificationEnabled()) {
							securityManagementService.initiatePasswordRecoveryRequest(
								user,
								user.hasPasswordHash() ? UserPasswordRecoveryRequestType.RESET : UserPasswordRecoveryRequestType.CREATION,
								UserPasswordRecoveryRequestInitiator.USER
							);
						}
						
						Session.get().success(getString("security.password.recovery.request.reset.validate.success"));
						
						throw CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor().newRestartResponseException();
					} catch (RestartResponseException e) {
						throw e;
					} catch (Exception e) {
						LOGGER.error("Error occurred while requesting password reset.", e);
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
		Detachables.detach(emailModel);
	}

}
