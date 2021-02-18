package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.confirm;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsJQueryPluginResourceReference;

public final class BootstrapConfirmJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final BootstrapConfirmJavaScriptResourceReference INSTANCE = new BootstrapConfirmJavaScriptResourceReference();

	private BootstrapConfirmJavaScriptResourceReference() {
		super("webjars/bootstrap5-override/current/js/dist/confirm.js");
	}

	public static BootstrapConfirmJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
