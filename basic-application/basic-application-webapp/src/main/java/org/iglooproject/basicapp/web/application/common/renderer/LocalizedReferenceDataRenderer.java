package org.iglooproject.basicapp.web.application.common.renderer;

import java.util.Locale;

import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.wicket.more.rendering.GenericLocalizedReferenceDataRenderer;
import org.iglooproject.wicket.more.rendering.Renderer;

public abstract class LocalizedReferenceDataRenderer extends Renderer<LocalizedReferenceData<?>> {

	private static final long serialVersionUID = -3042035624376063917L;

	private static final Renderer<LocalizedReferenceData<?>> INSTANCE = new LocalizedReferenceDataRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(LocalizedReferenceData<?> value, Locale locale) {
			return GenericLocalizedReferenceDataRenderer.get().render(value, locale);
		}
	}.nullsAsNull();

	private static final Renderer<LocalizedReferenceData<?>> CODE_LABEL = new LocalizedReferenceDataRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(LocalizedReferenceData<?> value, Locale locale) {
			String code = value.getCode();
			return (code != null ? code + " - " : "") + LocalizedReferenceDataRenderer.get().render(value, locale);
		}
	}.nullsAsNull();

	public static final Renderer<LocalizedReferenceData<?>> get() {
		return INSTANCE;
	}

	public static final Renderer<LocalizedReferenceData<?>> codeLabel() {
		return CODE_LABEL;
	}

	private LocalizedReferenceDataRenderer() {
	}

}
