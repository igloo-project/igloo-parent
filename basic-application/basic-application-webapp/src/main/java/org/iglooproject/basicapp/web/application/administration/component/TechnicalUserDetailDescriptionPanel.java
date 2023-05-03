package org.iglooproject.basicapp.web.application.administration.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.form.TechnicalUserPopup;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.DefaultPlaceholderPanel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.link.EmailLink;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;

public class TechnicalUserDetailDescriptionPanel extends GenericPanel<TechnicalUser> {

	private static final long serialVersionUID = 8651898170121443991L;

	@SpringBean
	private IUserService userService;

	public TechnicalUserDetailDescriptionPanel(String id, final IModel<? extends TechnicalUser> userModel) {
		super(id, userModel);
		
		TechnicalUserPopup editPopup = new TechnicalUserPopup("editPopup");
		add(editPopup);
		
		IModel<String> emailModel = BindingModel.of(userModel, Bindings.user().email());
		
		add(
			new CoreLabel("username", BindingModel.of(userModel, Bindings.user().username()))
				.showPlaceholder(),
			new BooleanIcon("enabled", BindingModel.of(userModel, Bindings.user().enabled())),
			new EmailLink("email", emailModel),
			new DefaultPlaceholderPanel("emailPlaceholder").condition(Condition.modelNotNull(emailModel)),
			new CoreLabel("locale", BindingModel.of(userModel, Bindings.user().locale()))
				.showPlaceholder(),
			new CoreLabel("creationDate", BindingModel.of(userModel, Bindings.user().creationDate()))
				.showPlaceholder(),
			new CoreLabel("lastUpdateDate", BindingModel.of(userModel, Bindings.user().lastUpdateDate()))
				.showPlaceholder(),
			new CoreLabel("lastLoginDate", BindingModel.of(userModel, Bindings.user().lastLoginDate()))
				.showPlaceholder()
		);
		
		add(
			new EnclosureContainer("actionsContainer")
				.anyChildVisible()
				.add(
					new BlankLink("edit")
						.add(
							new AjaxModalOpenBehavior(editPopup, MouseEvent.CLICK) {
								private static final long serialVersionUID = 1L;
								@Override
								protected void onShow(AjaxRequestTarget target) {
									editPopup.setUpEdit(getModelObject());
								}
							}
						)
				)
		);
	}

}
