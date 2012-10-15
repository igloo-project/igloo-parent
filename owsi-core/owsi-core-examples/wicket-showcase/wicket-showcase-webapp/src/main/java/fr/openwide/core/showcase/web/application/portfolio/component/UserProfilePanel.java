package fr.openwide.core.showcase.web.application.portfolio.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.events.MouseEvent;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserBinding;
import fr.openwide.core.showcase.web.application.portfolio.form.EditUserPopupPanel;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.image.BooleanGlyphicon;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.emailobfuscator.ObfuscatedEmailLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.AjaxOpenModalBehavior;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.DatePattern;

public class UserProfilePanel extends GenericPanel<User> {
	private static final long serialVersionUID = 646894189220818498L;
	
	private static final UserBinding USER_BINDING = new UserBinding();
	
	public UserProfilePanel(String id, IModel<User> userModel) {
		super(id, userModel);
		
		add(new Label("userName", BindingModel.of(userModel, USER_BINDING.userName())));
		
		add(new ObfuscatedEmailLink("emailLink", BindingModel.of(userModel, USER_BINDING.email()), true));
		
		add(new Label("phoneNumber", BindingModel.of(userModel, USER_BINDING.phoneNumber())));
		add(new Label("gsmNumber", BindingModel.of(userModel, USER_BINDING.gsmNumber())));
		add(new Label("faxNumber", BindingModel.of(userModel, USER_BINDING.faxNumber())));
		
		add(new DateLabel("creationDate", BindingModel.of(userModel, USER_BINDING.creationDate()),
				DatePattern.SHORT_DATETIME));
		add(new DateLabel("lastLoginDate", BindingModel.of(userModel, USER_BINDING.lastLoginDate()),
				DatePattern.SHORT_DATETIME));
		
		add(new BooleanGlyphicon("active", BindingModel.of(userModel, USER_BINDING.active())));
		
		// Edit user popup panel
		EditUserPopupPanel userEditPopupPanel = new EditUserPopupPanel("userEditPopupPanel", userModel);
		add(userEditPopupPanel);
		
		WebMarkupContainer editUserPopupLink = new WebMarkupContainer("editUserPopupLink");
		editUserPopupLink.add(new AjaxOpenModalBehavior(userEditPopupPanel, MouseEvent.CLICK) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onShow(AjaxRequestTarget target) {
				
			}
		});
		add(editUserPopupLink);
	}
}
