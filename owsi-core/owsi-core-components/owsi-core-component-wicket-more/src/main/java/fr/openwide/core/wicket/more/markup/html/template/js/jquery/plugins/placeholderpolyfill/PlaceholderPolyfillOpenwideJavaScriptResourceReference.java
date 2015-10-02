package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.placeholderpolyfill;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class PlaceholderPolyfillOpenwideJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1012203655427832428L;

	private static final PlaceholderPolyfillOpenwideJavaScriptResourceReference INSTANCE = new PlaceholderPolyfillOpenwideJavaScriptResourceReference();

	public PlaceholderPolyfillOpenwideJavaScriptResourceReference() {
		super(PlaceholderPolyfillOpenwideJavaScriptResourceReference.class, "jquery.placeholder.openwide.js");
	}

	public static PlaceholderPolyfillOpenwideJavaScriptResourceReference get() {
		return INSTANCE;
	}
}
