package org.iglooproject.basicapp.web.application.administration.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.administration.form.TechnicalUserPopup;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.link.EmailLink;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.DateLabel;
import org.iglooproject.wicket.more.markup.html.basic.DefaultPlaceholderPanel;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.util.DatePattern;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class TechnicalUserDetailDescriptionPanel extends GenericPanel<TechnicalUser> {

	private static final long serialVersionUID = 8651898170121443991L;

	@SpringBean
	private IUserService userService;

	public TechnicalUserDetailDescriptionPanel(String id, final IModel<? extends TechnicalUser> model) {
		super(id, model);
		
		TechnicalUserPopup editPopup = new TechnicalUserPopup("editPopup");
		add(editPopup);
		
		IModel<String> emailModel = BindingModel.of(model, Bindings.user().email());
		
		add(
			new BlankLink("edit")
				.add(new AjaxModalOpenBehavior(editPopup, MouseEvent.CLICK) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onShow(AjaxRequestTarget target) {
						editPopup.setUpEdit(getModelObject());
					}
				})
		);
		
		add(
			new CoreLabel("username", BindingModel.of(model, Bindings.user().username()))
				.showPlaceholder(),
			new BooleanIcon("active", BindingModel.of(model, Bindings.user().active())),
			new EmailLink("email", emailModel),
			new DefaultPlaceholderPanel("emailPlaceholder").condition(Condition.modelNotNull(emailModel)),
			new DateLabel("creationDate", BindingModel.of(model, Bindings.user().creationDate()), DatePattern.SHORT_DATETIME)
				.showPlaceholder(),
			new DateLabel("lastUpdateDate", BindingModel.of(model, Bindings.user().lastUpdateDate()), DatePattern.SHORT_DATETIME)
				.showPlaceholder(),
			new CoreLabel("locale", BindingModel.of(model, Bindings.user().locale()))
				.showPlaceholder(),
			new DateLabel("lastLoginDate", BindingModel.of(model, Bindings.user().lastLoginDate()), DatePattern.SHORT_DATETIME)
				.showPlaceholder()
		);
	}

}
