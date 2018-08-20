package org.iglooproject.wicket.more.css.lesscss;

import java.util.Locale;

import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.css.ICssResourceReference;

public class LessCssResourceReference extends ResourceReference implements ICssResourceReference {

	private static final long serialVersionUID = -1009491608247244399L;

	public LessCssResourceReference(Class<?> scope, String name) {
		this(scope, name, null, null, null);
	}

	public LessCssResourceReference(Class<?> scope, String name, Locale locale, String style, String variation) {
		super(scope, name, locale, style, variation);
	}

	@Override
	public LessCssResource getResource() {
		return new LessCssResource(getScope(), getName(), getLocale(), getStyle(), getVariation());
	}

}