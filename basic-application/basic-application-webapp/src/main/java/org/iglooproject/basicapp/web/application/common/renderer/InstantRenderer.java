package org.iglooproject.basicapp.web.application.common.renderer;

import java.time.Instant;
import java.util.Locale;

import org.iglooproject.basicapp.core.util.time.DateTimePattern;

import igloo.wicket.renderer.Renderer;

public abstract class InstantRenderer extends Renderer<Instant> {

	private static final long serialVersionUID = 6997048072226585653L;

	private static final Renderer<Instant> INSTANCE = new InstantRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(Instant value, Locale locale) {
			return Renderer.fromDateTimePattern(DateTimePattern.SHORT_DATETIME).render(value, locale);
		}
	}
		.nullsAsNull();

	public static Renderer<Instant> get() {
		return INSTANCE;
	}

	private InstantRenderer() {
	}

}

