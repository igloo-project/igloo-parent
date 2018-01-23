package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.confirm;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapConfirmJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final BootstrapConfirmJavaScriptResourceReference INSTANCE = new BootstrapConfirmJavaScriptResourceReference();

	private BootstrapConfirmJavaScriptResourceReference() {
		super(BootstrapConfirmJavaScriptResourceReference.class, "bootstrap-confirm.js");
	}

	public static BootstrapConfirmJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
