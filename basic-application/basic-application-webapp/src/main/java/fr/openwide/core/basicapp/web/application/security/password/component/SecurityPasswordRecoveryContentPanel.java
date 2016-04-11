package fr.openwide.core.basicapp.web.application.security.password.component;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import fr.openwide.core.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.SecurityUserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.validator.EmailExistsValidator;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;

public class SecurityPasswordRecoveryContentPanel extends Panel {

	private static final long serialVersionUID = 547223775134254240L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPasswordRecoveryContentPanel.class);

	private final IModel<String> emailModel = Model.of("");

	@SpringBean
	private IUserService userService;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	public SecurityPasswordRecoveryContentPanel(String wicketId) {
		super(wicketId);
		
		Form<?> form = new Form<Void>("form");
		
		add(form);
		form.add(
				new RequiredTextField<>("email", emailModel)
						.setLabel(new ResourceModel("business.user.email"))
						.add(EmailAddressValidator.getInstance())
						.add(EmailExistsValidator.get())
						.add(new LabelPlaceholderBehavior()),
				
				new AjaxButton("validate", form) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						try {
							User user = userService.getByEmailCaseInsensitive(emailModel.getObject());
							securityManagementService.initiatePasswordRecoveryRequest(
									user,
									UserPasswordRecoveryRequestType.RESET,
									UserPasswordRecoveryRequestInitiator.USER
							);
							
							getSession().success(getString("security.password.recovery.validate.success"));
							
							throw SecurityUserTypeDescriptor.USER.signInPageLinkDescriptor()
									.newRestartResponseException();
						} catch (RestartResponseException e) {
							throw e;
						} catch (Exception e) {
							LOGGER.error("Error occurred while recovering password", e);
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
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		emailModel.detach();
	}
}
