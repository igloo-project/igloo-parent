package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.modal;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsJQueryPluginResourceReference;

public final class BootstrapModalMoreJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final BootstrapModalMoreJavaScriptResourceReference INSTANCE = new BootstrapModalMoreJavaScriptResourceReference();

	private BootstrapModalMoreJavaScriptResourceReference() {
		super("webjars/bootstrap5-override/current/js/dist/modal-more.js");
	}

	public static BootstrapModalMoreJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
