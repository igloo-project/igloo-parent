package org.iglooproject.basicapp.web.application.administration.component;

import static org.iglooproject.commons.util.functional.Predicates2.isTrue;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import org.iglooproject.basicapp.core.business.user.model.atomic.UserPasswordRecoveryRequestType;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.administration.form.AbstractUserPopup;
import org.iglooproject.basicapp.web.application.administration.form.UserPasswordUpdatePopup;
import org.iglooproject.basicapp.web.application.administration.form.UserPopup;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.link.EmailLink;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.action.AbstractAjaxAction;
import org.iglooproject.wicket.more.markup.html.basic.DateLabel;
import org.iglooproject.wicket.more.markup.html.basic.DefaultPlaceholderPanel;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.confirm.component.AjaxConfirmLink;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.util.DatePattern;

public class UserProfilePanel<U extends User> extends GenericPanel<U> {

	private static final long serialVersionUID = 8651898170121443991L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserProfilePanel.class);

	@SpringBean
	private IUserService userService;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	public UserProfilePanel(String id, final IModel<U> userModel, UserTypeDescriptor<U> typeDescriptor) {
		super(id, userModel);
		
		AbstractUserPopup<U> updatePopup = createUpdatePopup("updatePopup", getModel(), typeDescriptor);
		
		UserPasswordUpdatePopup<U> passwordUpdatePopup = new UserPasswordUpdatePopup<>("passwordUpdatePopup", getModel());
		
		IModel<String> confirmationTextModel = new StringResourceModel("administration.user.disable.confirmation.text")
				.setParameters(userModel.getObject().getFullName());
		
		IModel<String> emailModel = BindingModel.of(userModel, Bindings.user().email());
		
		add(
				updatePopup,
				new BlankLink("updateButton")
						.add(new AjaxModalOpenBehavior(updatePopup, MouseEvent.CLICK)),
				
				passwordUpdatePopup,
				new BlankLink("passwordUpdateButton")
						.add(new AjaxModalOpenBehavior(passwordUpdatePopup, MouseEvent.CLICK))
						.add(
								Condition.predicate(
										Model.of(securityManagementService.getOptions(getModelObject()).isPasswordAdminUpdateEnabled()),
										isTrue()
								).thenShow()
						),
				
				AjaxConfirmLink.<U>build()
						.title(new ResourceModel("administration.user.password.recovery.reset.confirmation.title"))
						.content(new StringResourceModel("administration.user.password.recovery.reset.confirmation.text").setModel(userModel))
						.confirm()
						.onClick(new AbstractAjaxAction() {
							private static final long serialVersionUID = 1L;
							@Override
							public void execute(AjaxRequestTarget target) {
								try {
									securityManagementService.initiatePasswordRecoveryRequest(userModel.getObject(),
											UserPasswordRecoveryRequestType.RESET,
											UserPasswordRecoveryRequestInitiator.ADMIN,
											BasicApplicationSession.get().getUser()
									);
									getSession().success(getString("administration.user.password.recovery.reset.success"));
									target.add(getPage());
								} catch (Exception e) {
									LOGGER.error("Error occured while sending a password recovery request", e);
									getSession().error(getString("common.error.unexpected"));
								}
								FeedbackUtils.refreshFeedback(target, getPage());
							}
						})
						.create("passwordReset", userModel)
						.add(
								Condition.predicate(
										Model.of(securityManagementService.getOptions(getModelObject()).isPasswordAdminRecoveryEnabled()),
										isTrue()
								).thenShow()
						),
				
				new Link<U>("enable", userModel) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						try {
							userService.setActive(getModelObject(), true);
							getSession().success(getString("administration.user.enable.success"));
						} catch (Exception e) {
							LOGGER.error("Error occured while enabling user", e);
							getSession().error(getString("common.error.unexpected"));
						}
					}
					@Override
					protected void onConfigure() {
						super.onConfigure();
						setVisible(!getModelObject().isActive());
					}
				},
				
				AjaxConfirmLink.<U>build()
						.title(new ResourceModel("administration.user.disable.confirmation.title"))
						.content(confirmationTextModel)
						.confirm()
						.onClick(
								new AbstractAjaxAction() {
									private static final long serialVersionUID = 1L;
									@Override
									public void execute(AjaxRequestTarget target) {
										try {
											userService.setActive(userModel.getObject(), false);
											getSession().success(getString("administration.user.disable.success"));
										} catch (Exception e) {
											LOGGER.error("Error occured while disabling user", e);
											getSession().error(getString("common.error.unexpected"));
										}
										target.add(getPage());
										FeedbackUtils.refreshFeedback(target, getPage());
									}
								}
						)
						.create("disable", userModel)
						.add(
								new Condition() {
									private static final long serialVersionUID = 1L;
									@Override
									public boolean applies() {
										User displayedUser = getModelObject();
										User currentUser = BasicApplicationSession.get().getUser();
										return !displayedUser.equals(currentUser) && displayedUser.isActive();
									}
								}.thenShowInternal()
						),
				
				new Label("userName", BindingModel.of(userModel, Bindings.user().userName())),
				new BooleanIcon("active", BindingModel.of(userModel, Bindings.user().active())),
				new EmailLink("email", emailModel),
				new DefaultPlaceholderPanel("emailPlaceholder").condition(Condition.modelNotNull(emailModel)),
				new DateLabel("creationDate", BindingModel.of(userModel, Bindings.user().creationDate()),
						DatePattern.SHORT_DATETIME).showPlaceholder(),
				new DateLabel("lastUpdateDate", BindingModel.of(userModel, Bindings.user().lastUpdateDate()),
						DatePattern.SHORT_DATETIME).showPlaceholder(),
				new CoreLabel("locale", BindingModel.of(userModel, Bindings.user().locale())).showPlaceholder(),
				new DateLabel("lastLoginDate", BindingModel.of(userModel, Bindings.user().lastLoginDate()),
						DatePattern.SHORT_DATETIME).showPlaceholder()
		);
	}
	
	protected AbstractUserPopup<U> createUpdatePopup(String wicketId, IModel<U> model, UserTypeDescriptor<U> typeDescriptor) {
		return new UserPopup<U>(wicketId, getModel(), typeDescriptor);
	}
}
