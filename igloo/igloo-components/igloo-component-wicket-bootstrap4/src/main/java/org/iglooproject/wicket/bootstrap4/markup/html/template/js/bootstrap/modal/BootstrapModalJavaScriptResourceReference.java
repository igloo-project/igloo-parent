package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class BootstrapModalJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = -8799742276479282371L;

	private static final BootstrapModalJavaScriptResourceReference INSTANCE = new BootstrapModalJavaScriptResourceReference();

	private BootstrapModalJavaScriptResourceReference() {
		super("bootstrap-override/current/js/dist/modal.js");
	}

	public static BootstrapModalJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
