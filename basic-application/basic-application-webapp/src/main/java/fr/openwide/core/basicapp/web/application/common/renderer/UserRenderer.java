package fr.openwide.core.basicapp.web.application.common.renderer;

import java.util.Locale;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.wicket.more.rendering.Renderer;

public final class UserRenderer extends Renderer<User> {

	private static final long serialVersionUID = 5707691630314666729L;

	private static final UserRenderer INSTANCE = new UserRenderer();

	public static UserRenderer get() {
		return INSTANCE;
	}

	private UserRenderer() { }

	@Override
	public String render(User value, Locale locale) {
		if (value == null) {
			return null;
		} else {
			return value.getFullName();
		}
	}

}
