package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsCoreJQueryPluginResourceReference;

public final class BootstrapUtilJavaScriptResourceReference extends WebjarsCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final BootstrapUtilJavaScriptResourceReference INSTANCE = new BootstrapUtilJavaScriptResourceReference();

	private BootstrapUtilJavaScriptResourceReference() {
		super("webjars/bootstrap/current/js/dist/util.js");
	}

	public static BootstrapUtilJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
