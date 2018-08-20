package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsJQueryPluginResourceReference;

public final class BootstrapModalJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -8799742276479282371L;

	private static final BootstrapModalJavaScriptResourceReference INSTANCE = new BootstrapModalJavaScriptResourceReference();

	private BootstrapModalJavaScriptResourceReference() {
		super("bootstrap-override/current/js/dist/modal.js");
	}

	public static BootstrapModalJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
