package fr.openwide.core.basicapp.web.application.common.renderer;

import java.util.Locale;

import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.wicket.more.rendering.Renderer;

public final class UserGroupRenderer extends Renderer<UserGroup> {

	private static final long serialVersionUID = 5707691630314666729L;

	private static final UserGroupRenderer INSTANCE = new UserGroupRenderer();

	public static UserGroupRenderer get() {
		return INSTANCE;
	}

	private UserGroupRenderer() {
	}

	@Override
	public String render(UserGroup value, Locale locale) {
		if (value == null) {
			return null;
		} else {
			return value.getName();
		}
	}

}
