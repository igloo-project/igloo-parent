package org.iglooproject.basicapp.web.application.security.password.component;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;

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
