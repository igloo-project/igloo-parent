package fr.openwide.core.showcase.web.application.portfolio.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.events.MouseEvent;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.util.binding.Bindings;
import fr.openwide.core.showcase.web.application.portfolio.form.EditUserPopupPanel;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.image.BooleanIcon;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.emailobfuscator.ObfuscatedEmailLink;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.DatePattern;

public class UserProfilePanel extends GenericPanel<User> {
	private static final long serialVersionUID = 646894189220818498L;
	
	public UserProfilePanel(String id, IModel<User> userModel) {
		super(id, userModel);
		
		add(new CoreLabel("userName", BindingModel.of(userModel, Bindings.user().userName())).showPlaceholder());
		
		add(new ObfuscatedEmailLink("emailLink", BindingModel.of(userModel, Bindings.user().email()), true));
		
		add(new CoreLabel("phoneNumber", BindingModel.of(userModel, Bindings.user().phoneNumber())).showPlaceholder());
		add(new CoreLabel("gsmNumber", BindingModel.of(userModel, Bindings.user().gsmNumber())).showPlaceholder());
		add(new CoreLabel("faxNumber", BindingModel.of(userModel, Bindings.user().faxNumber())).showPlaceholder());
		
		add(new DateLabel("creationDate", BindingModel.of(userModel, Bindings.user().creationDate()),
				DatePattern.SHORT_DATETIME).showPlaceholder());
		add(new DateLabel("lastLoginDate", BindingModel.of(userModel, Bindings.user().lastLoginDate()),
				DatePattern.SHORT_DATETIME).showPlaceholder());
		
		add(new BooleanIcon("active", BindingModel.of(userModel, Bindings.user().active())));
		
		// Edit user popup panel
		EditUserPopupPanel userEditPopupPanel = new EditUserPopupPanel("userEditPopupPanel", userModel);
		add(userEditPopupPanel);
		
		WebMarkupContainer editUserPopupLink = new WebMarkupContainer("editUserPopupLink");
		editUserPopupLink.add(new AjaxModalOpenBehavior(userEditPopupPanel, MouseEvent.CLICK));
		add(editUserPopupLink);
	}
}
