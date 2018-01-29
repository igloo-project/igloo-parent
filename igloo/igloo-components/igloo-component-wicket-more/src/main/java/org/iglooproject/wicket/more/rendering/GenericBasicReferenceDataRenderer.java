package org.iglooproject.wicket.more.rendering;

import java.util.Locale;

import org.iglooproject.jpa.more.business.referencedata.model.GenericBasicReferenceData;

public abstract class GenericBasicReferenceDataRenderer extends Renderer<GenericBasicReferenceData<?>> {

	private static final long serialVersionUID = -3042035624376063917L;

	private static final Renderer<GenericBasicReferenceData<?>> INSTANCE = new GenericBasicReferenceDataRenderer() {
		private static final long serialVersionUID = 1L;
		@Override
		public String render(GenericBasicReferenceData<?> value, Locale locale) {
			return value.getLabel();
		}
	}.nullsAsNull();

	public static final Renderer<GenericBasicReferenceData<?>> get() {
		return INSTANCE;
	}

	private GenericBasicReferenceDataRenderer() {
	}

}

