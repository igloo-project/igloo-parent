package fr.openwide.core.showcase.web.application.portfolio.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserBinding;
import fr.openwide.core.wicket.markup.html.link.EmailLink;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.image.BooleanImage;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.DatePattern;

public class UserIdentityCardPanel extends GenericPanel<User> {
	private static final long serialVersionUID = 646894189220818498L;
	
	private static final UserBinding USER_BINDING = new UserBinding();
	
	public UserIdentityCardPanel(String id, IModel<User> userModel) {
		super(id, userModel);
		
		add(new Label("title", BindingModel.of(userModel, USER_BINDING.fullName())));
		
		add(new Label("fullName", BindingModel.of(userModel, USER_BINDING.fullName())));
		add(new Label("userName", BindingModel.of(userModel, USER_BINDING.userName())));
		
		add(new EmailLink("emailLink", BindingModel.of(userModel, USER_BINDING.email()),
				BindingModel.of(userModel, USER_BINDING.email())));
		
		add(new Label("phoneNumber", BindingModel.of(userModel, USER_BINDING.phoneNumber())));
		add(new Label("gsmNumber", BindingModel.of(userModel, USER_BINDING.gsmNumber())));
		add(new Label("faxNumber", BindingModel.of(userModel, USER_BINDING.faxNumber())));
		
		add(new DateLabel("creationDate", BindingModel.of(userModel, USER_BINDING.creationDate()),
				DatePattern.SHORT_DATETIME));
		add(new DateLabel("lastLoginDate", BindingModel.of(userModel, USER_BINDING.lastLoginDate()),
				DatePattern.SHORT_DATETIME));
		
		BooleanImage activeImage = new BooleanImage("active", BindingModel.of(userModel, USER_BINDING.active()));
		
		boolean active = getModelObject().isActive();
		String tipsyProperty = active ? "common.active" : "common.inactive";
		
		activeImage.add(new AttributeModifier("alt", new ResourceModel(tipsyProperty, "")));
		activeImage.add(new AttributeModifier("data-tooltip", new ResourceModel(tipsyProperty, "")));
		add(activeImage);
	}
}
