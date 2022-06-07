package org.iglooproject.basicapp.web.application.common.renderer;

import java.util.Locale;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.wicket.renderer.Renderer;

public abstract class UserRenderer extends Renderer<User> {

	private static final long serialVersionUID = 5707691630314666729L;

	private static final Renderer<User> INSTANCE = new UserRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(User value, Locale locale) {
			return value.getFullName();
		}
	}.nullsAsNull();

	public static Renderer<User> get() {
		return INSTANCE;
	}

	private UserRenderer() {
	}

}
