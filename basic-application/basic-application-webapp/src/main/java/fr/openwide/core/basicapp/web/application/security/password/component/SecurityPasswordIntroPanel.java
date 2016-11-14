package fr.openwide.core.basicapp.web.application.security.password.component;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.condition.Condition;

public class SecurityPasswordIntroPanel extends GenericPanel<User> {

	private static final long serialVersionUID = 1L;

	public SecurityPasswordIntroPanel(String wicketId, String resourceKey) {
		super(wicketId);
		
		IModel<String> introModel = new ResourceModel(resourceKey);
		Component introLabel = new CoreLabel("introLabel", introModel).hideIfEmpty();
		add(introLabel);
		add(Condition.componentVisible(introLabel).thenShowInternal());
	}

}
