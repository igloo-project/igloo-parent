package org.iglooproject.basicapp.web.application.common.renderer;

import java.util.Locale;

import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.wicket.more.rendering.LocalizedTextRenderer;

import igloo.wicket.renderer.Renderer;

public abstract class ReferenceDataRenderer extends Renderer<ReferenceData<?>> {

	private static final long serialVersionUID = -3042035624376063917L;

	private static final Renderer<ReferenceData<?>> INSTANCE = new ReferenceDataRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(ReferenceData<?> value, Locale locale) {
			return LocalizedTextRenderer.get().render(value.getLabel(), locale);
		}
	}.nullsAsNull();

	public static final Renderer<ReferenceData<?>> get() {
		return INSTANCE;
	}

	private ReferenceDataRenderer() {
	}

}
