package org.iglooproject.basicapp.web.application.common.renderer;

import java.util.Locale;

import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.wicket.more.rendering.Renderer;

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
