package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.tab;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsJQueryPluginResourceReference;

public final class BootstrapTabMoreJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final BootstrapTabMoreJavaScriptResourceReference INSTANCE = new BootstrapTabMoreJavaScriptResourceReference();

	private BootstrapTabMoreJavaScriptResourceReference() {
		super("webjars/bootstrap5-override/current/js/dist/tab-more.js");
	}

	public static BootstrapTabMoreJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
