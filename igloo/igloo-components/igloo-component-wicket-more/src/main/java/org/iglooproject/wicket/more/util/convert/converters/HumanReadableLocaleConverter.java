package org.iglooproject.wicket.more.util.convert.converters;

import org.iglooproject.wicket.more.rendering.LocaleRenderer;

/**
 * @deprecated Use {@link LocaleRenderer} instead.
 */
@Deprecated
public class HumanReadableLocaleConverter extends LocaleRenderer {

	private static final long serialVersionUID = 3616234068185075603L;
	
	private static final HumanReadableLocaleConverter INSTANCE = new HumanReadableLocaleConverter();
	public static HumanReadableLocaleConverter get() {
		return INSTANCE;
	}
	
	private HumanReadableLocaleConverter() { }

}
