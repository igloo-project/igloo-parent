package org.iglooproject.wicket.more.rendering;

import java.util.Locale;
import java.util.Optional;

import org.apache.wicket.Session;

import igloo.wicket.renderer.Renderer;

public abstract class LocaleRenderer extends Renderer<Locale> {

	private static final long serialVersionUID = 3616234068185075603L;

	private static final Renderer<Locale> INSTANCE = new LocaleRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(Locale value, Locale locale) {
			return value.getDisplayName(Optional.ofNullable(locale).orElse(Session.get().getLocale()));
		}
	}.nullsAsNull();

	public static Renderer<Locale> get() {
		return INSTANCE;
	}

	private LocaleRenderer() {
	}

}
