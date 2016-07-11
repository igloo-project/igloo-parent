package fr.openwide.core.basicapp.web.application.common.renderer;

import java.util.Locale;

import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.wicket.more.rendering.Renderer;

public final class AuthorityRenderer extends Renderer<Authority> {

	private static final long serialVersionUID = 5707691630314666729L;

	private static final Renderer<Authority> INSTANCE = new AuthorityRenderer();

	private AuthorityRenderer() {
	}

	public static Renderer<Authority> get() {
		return INSTANCE;
	}

	@Override
	public String render(Authority value, Locale locale) {
		return getString("administration.usergroup.authority." + value.getName(), locale);
	}

}
