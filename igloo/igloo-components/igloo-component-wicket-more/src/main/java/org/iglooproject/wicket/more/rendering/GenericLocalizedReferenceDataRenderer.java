package org.iglooproject.wicket.more.rendering;

import java.util.Locale;

import org.iglooproject.jpa.more.business.referencedata.model.GenericLocalizedReferenceData;

public abstract class GenericLocalizedReferenceDataRenderer extends Renderer<GenericLocalizedReferenceData<?, ?>> {

	private static final long serialVersionUID = -3042035624376063917L;

	private static final Renderer<GenericLocalizedReferenceData<?, ?>> INSTANCE = new GenericLocalizedReferenceDataRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(GenericLocalizedReferenceData<?, ?> value, Locale locale) {
			return LocalizedTextRenderer.get().render(value.getLabel(), locale);
		}
	}.nullsAsNull();

	public static final Renderer<GenericLocalizedReferenceData<?, ?>> get() {
		return INSTANCE;
	}

	private GenericLocalizedReferenceDataRenderer() {
	}

}

