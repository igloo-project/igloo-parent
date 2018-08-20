package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.placeholderpolyfill;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class PlaceholderPolyfillIglooJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1012203655427832428L;

	private static final PlaceholderPolyfillIglooJavaScriptResourceReference INSTANCE = new PlaceholderPolyfillIglooJavaScriptResourceReference();

	public PlaceholderPolyfillIglooJavaScriptResourceReference() {
		super(PlaceholderPolyfillIglooJavaScriptResourceReference.class, "jquery.placeholder.igloo.js");
	}

	public static PlaceholderPolyfillIglooJavaScriptResourceReference get() {
		return INSTANCE;
	}
}
