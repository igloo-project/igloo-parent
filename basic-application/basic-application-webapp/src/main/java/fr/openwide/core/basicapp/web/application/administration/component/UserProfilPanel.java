package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.events.MouseEvent;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Binding;
import fr.openwide.core.basicapp.web.application.administration.form.UserFormPopupPanel;
import fr.openwide.core.wicket.markup.html.link.EmailLink;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.image.BooleanGlyphicon;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.AjaxOpenModalBehavior;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.DatePattern;

public class UserProfilPanel extends GenericPanel<User> {

	private static final long serialVersionUID = 8651898170121443991L;

	public UserProfilPanel(String id, IModel<User> userModel) {
		super(id, userModel);
		
		add(new Label("userName", BindingModel.of(userModel, Binding.user().userName())));
		add(new BooleanGlyphicon("active", BindingModel.of(userModel, Binding.user().active())));
		add(new EmailLink("email", BindingModel.of(userModel, Binding.user().email()),
				BindingModel.of(userModel, Binding.user().email())));
		add(new DateLabel("creationDate", BindingModel.of(userModel, Binding.user().creationDate()),
				DatePattern.SHORT_DATETIME));
		add(new DateLabel("lastLoginDate", BindingModel.of(userModel, Binding.user().lastLoginDate()),
				DatePattern.SHORT_DATETIME));
		add(new DateLabel("lastUpdateDate", BindingModel.of(userModel, Binding.user().lastUpdateDate()),
				DatePattern.SHORT_DATETIME));
		
		// User update popup
		UserFormPopupPanel userUpdatePanel = new UserFormPopupPanel("userUpdatePopupPanel", getModel());
		add(userUpdatePanel);
		
		Button updateUser = new Button("updateUser");
		updateUser.add(new AjaxOpenModalBehavior(userUpdatePanel, MouseEvent.CLICK) {
			private static final long serialVersionUID = 5414159291353181776L;
			
			@Override
			protected void onShow(AjaxRequestTarget target) {
			}
		});
		add(updateUser);
	}
}
