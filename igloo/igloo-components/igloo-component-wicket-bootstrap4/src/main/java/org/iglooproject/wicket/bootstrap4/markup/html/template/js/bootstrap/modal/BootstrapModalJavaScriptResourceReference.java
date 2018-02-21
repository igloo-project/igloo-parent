package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapModalJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -8799742276479282371L;

	private static final BootstrapModalJavaScriptResourceReference INSTANCE = new BootstrapModalJavaScriptResourceReference();

	private BootstrapModalJavaScriptResourceReference() {
		super(BootstrapModalJavaScriptResourceReference.class, "modal-patch.js");
	}

	public static BootstrapModalJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
