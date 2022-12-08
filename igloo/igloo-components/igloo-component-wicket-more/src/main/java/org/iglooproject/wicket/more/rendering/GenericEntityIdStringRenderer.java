package org.iglooproject.wicket.more.rendering;

import java.util.Locale;

import org.iglooproject.jpa.business.generic.model.GenericEntity;

import igloo.wicket.renderer.Renderer;

public final class GenericEntityIdStringRenderer extends Renderer<GenericEntity<?, ?>> {

	private static final long serialVersionUID = 7873816954526901214L;
	
	private static final GenericEntityIdStringRenderer INSTANCE = new GenericEntityIdStringRenderer();

	public static final GenericEntityIdStringRenderer get() {
		return INSTANCE;
	}

	private GenericEntityIdStringRenderer() {
	}

	@Override
	public String render(GenericEntity<?, ?> value, Locale locale) {
		if (value == null) {
			return "-1";
		} else {
			return value.getId().toString();
		}
	}
	
	public String render(GenericEntity<?, ?> value) {
		return render(value, null);
	}

}
