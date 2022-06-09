package org.iglooproject.basicapp.web.application.common.renderer;

import java.util.Locale;

import org.iglooproject.jpa.security.business.authority.model.Authority;

import igloo.wicket.renderer.Renderer;

public abstract class AuthorityRenderer extends Renderer<Authority> {

	private static final long serialVersionUID = 5707691630314666729L;

	private static final Renderer<Authority> INSTANCE = new AuthorityRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(Authority value, Locale locale) {
			return getString("administration.userGroup.authority." + value.getName(), locale);
		}
	}.nullsAsNull();

	public static Renderer<Authority> get() {
		return INSTANCE;
	}

	private AuthorityRenderer() {
	}

}
