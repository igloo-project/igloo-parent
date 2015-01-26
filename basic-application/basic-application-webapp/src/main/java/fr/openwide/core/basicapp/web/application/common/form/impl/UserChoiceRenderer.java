package fr.openwide.core.basicapp.web.application.common.form.impl;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

import fr.openwide.core.basicapp.core.business.user.model.User;

public final class UserChoiceRenderer implements IChoiceRenderer<User> {
	private static final long serialVersionUID = 1L;
	
	private static final UserChoiceRenderer INSTANCE = new UserChoiceRenderer();
	public static UserChoiceRenderer get() {
		return INSTANCE;
	}
	
	private UserChoiceRenderer() {
	}
	
	@Override
	public Object getDisplayValue(User user) {
		return user != null ? user.getFullName() : "";
	}
	
	@Override
	public String getIdValue(User user, int index) {
		return user != null ? user.getId().toString() : "-1";
	}
}