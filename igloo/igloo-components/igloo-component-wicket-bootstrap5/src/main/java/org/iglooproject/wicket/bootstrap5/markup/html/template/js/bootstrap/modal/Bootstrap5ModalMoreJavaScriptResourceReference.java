package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.modal;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsJQueryPluginResourceReference;

public final class Bootstrap5ModalMoreJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final Bootstrap5ModalMoreJavaScriptResourceReference INSTANCE = new Bootstrap5ModalMoreJavaScriptResourceReference();

	private Bootstrap5ModalMoreJavaScriptResourceReference() {
		super("webjars/bootstrap5-override/current/js/dist/modal-more.js");
	}

	public static Bootstrap5ModalMoreJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
