package fr.openwide.core.showcase.core.util.binding;

import fr.openwide.core.showcase.core.business.user.model.UserBinding;

public final class Binding {

	private Binding() { }
	
	private static final UserBinding USER = new UserBinding();
	
	public static UserBinding user() {
		return USER;
	}

}
