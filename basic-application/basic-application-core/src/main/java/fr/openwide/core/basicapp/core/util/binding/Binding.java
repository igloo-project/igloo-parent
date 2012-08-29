package fr.openwide.core.basicapp.core.util.binding;

import fr.openwide.core.basicapp.core.business.user.model.UserBinding;
import fr.openwide.core.basicapp.core.business.user.model.UserGroupBinding;

public final class Binding {

	private static final UserBinding USER = new UserBinding();

	private static final UserGroupBinding USER_GROUP = new UserGroupBinding();

	public static UserBinding user() {
		return USER;
	}

	public static UserGroupBinding userGroup() {
		return USER_GROUP;
	}
	
	private Binding() {
	}
}
