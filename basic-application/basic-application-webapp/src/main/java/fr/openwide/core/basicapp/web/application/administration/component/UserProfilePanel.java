package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.administration.form.AbstractUserPopup;
import fr.openwide.core.basicapp.web.application.administration.form.UserPasswordUpdatePopup;
import fr.openwide.core.basicapp.web.application.administration.form.UserPopup;
import fr.openwide.core.basicapp.web.application.administration.util.AdministrationTypeUser;
import fr.openwide.core.wicket.markup.html.link.EmailLink;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.ComponentBooleanProperty;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.LocaleLabel;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.image.BooleanIcon;
import fr.openwide.core.wicket.more.markup.html.link.BlankLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.util.AjaxResponseAction;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.DatePattern;

public class UserProfilePanel<U extends User> extends GenericPanel<U> {

	private static final long serialVersionUID = 8651898170121443991L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserProfilePanel.class);

	@SpringBean
	private IUserService userService;

	public UserProfilePanel(String id, final IModel<U> userModel, AdministrationTypeUser<U> type) {
		super(id, userModel);
		
		AbstractUserPopup<U> updatePopup = createUpdatePopup("updatePopup", getModel(), type);
		
		UserPasswordUpdatePopup<U> passwordUpdatePopup = new UserPasswordUpdatePopup<>("passwordUpdatePopup", getModel());
		
		IModel<String> confirmationTextModel = new StringResourceModel(
				"administration.user.disable.confirmation.text", null, 
				new Object[] { userModel.getObject().getFullName() }
		);
		
		add(
				updatePopup,
				new BlankLink("updateButton")
						.add(new AjaxModalOpenBehavior(updatePopup, MouseEvent.CLICK)),
				
				passwordUpdatePopup,
				new BlankLink("passwordUpdateButton")
						.add(new AjaxModalOpenBehavior(passwordUpdatePopup, MouseEvent.CLICK)),
				
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
				
				AjaxConfirmLink.build("disable", userModel)
						.title(new ResourceModel("administration.user.disable.confirmation.title"))
						.content(confirmationTextModel)
						.confirm()
						.onClick(
								new AjaxResponseAction() {
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
						.create()
						.add(
								new EnclosureBehavior(ComponentBooleanProperty.VISIBLE).condition(new Condition() {
									private static final long serialVersionUID = 1L;
									@Override
									public boolean applies() {
										User displayedUser = getModelObject();
										User currentUser = BasicApplicationSession.get().getUser();
										return !displayedUser.equals(currentUser) && displayedUser.isActive();
									}
								})
						),
				
				new Label("userName", BindingModel.of(userModel, Bindings.user().userName())),
				new BooleanIcon("active", BindingModel.of(userModel, Bindings.user().active())),
				new EmailLink("email", BindingModel.of(userModel, Bindings.user().email())),
				new DateLabel("creationDate", BindingModel.of(userModel, Bindings.user().creationDate()),
						DatePattern.SHORT_DATETIME),
				new DateLabel("lastUpdateDate", BindingModel.of(userModel, Bindings.user().lastUpdateDate()),
						DatePattern.SHORT_DATETIME),
				new LocaleLabel("locale", BindingModel.of(userModel, Bindings.user().locale())),
				new DateLabel("lastLoginDate", BindingModel.of(userModel, Bindings.user().lastLoginDate()),
						DatePattern.SHORT_DATETIME)
		);
	}
	
	protected AbstractUserPopup<U> createUpdatePopup(String wicketId, IModel<U> model, AdministrationTypeUser<U> type) {
		return new UserPopup<U>(wicketId, getModel(), type);
	}
}
