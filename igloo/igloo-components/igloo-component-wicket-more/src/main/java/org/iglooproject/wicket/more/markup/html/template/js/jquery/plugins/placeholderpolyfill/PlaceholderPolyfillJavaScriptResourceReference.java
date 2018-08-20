package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.placeholderpolyfill;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class PlaceholderPolyfillJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1012203655427832428L;

	private static final PlaceholderPolyfillJavaScriptResourceReference INSTANCE = new PlaceholderPolyfillJavaScriptResourceReference();

	public PlaceholderPolyfillJavaScriptResourceReference() {
		super(PlaceholderPolyfillJavaScriptResourceReference.class, "jquery.placeholder.js");
	}

	public static PlaceholderPolyfillJavaScriptResourceReference get() {
		return INSTANCE;
	}
}
