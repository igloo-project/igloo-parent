package org.iglooproject.basicapp.web.application.profile.component;

import static org.iglooproject.commons.util.functional.Predicates2.isTrue;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.administration.form.UserPasswordUpdatePopup;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.link.EmailLink;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.DateLabel;
import org.iglooproject.wicket.more.markup.html.basic.DefaultPlaceholderPanel;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.util.DatePattern;

public class ProfileInformationPanel extends GenericPanel<User> {

	private static final long serialVersionUID = -1923855993008983060L;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	public ProfileInformationPanel(String id, IModel<User> userModel) {
		super(id, userModel);
		
		UserPasswordUpdatePopup<User> passwordUpdatePopup = new UserPasswordUpdatePopup<>("passwordUpdatePopup", userModel);
		
		IModel<String> emailModel = BindingModel.of(userModel, Bindings.user().email());
		
		add(
				passwordUpdatePopup,
				new BlankLink("passwordUpdateButton")
						.add(new AjaxModalOpenBehavior(passwordUpdatePopup, MouseEvent.CLICK))
						.add(
								Condition.predicate(
										Model.of(securityManagementService.getOptions(BasicApplicationSession.get().getUser()).isPasswordUserUpdateEnabled()),
										isTrue()
								).thenShow()
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

}
