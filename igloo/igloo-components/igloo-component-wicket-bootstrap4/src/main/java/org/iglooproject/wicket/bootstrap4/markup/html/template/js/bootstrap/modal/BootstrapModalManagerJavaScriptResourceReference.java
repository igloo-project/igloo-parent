package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapModalManagerJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final BootstrapModalManagerJavaScriptResourceReference INSTANCE = new BootstrapModalManagerJavaScriptResourceReference();

	private BootstrapModalManagerJavaScriptResourceReference() {
		super(BootstrapModalManagerJavaScriptResourceReference.class, "bootstrap-modalmanager.js");
	}

	public static BootstrapModalManagerJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
