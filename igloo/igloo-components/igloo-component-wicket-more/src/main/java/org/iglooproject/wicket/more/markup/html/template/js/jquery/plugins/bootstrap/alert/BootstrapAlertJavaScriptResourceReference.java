package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.alert;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapAlertJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;
	
	private static final BootstrapAlertJavaScriptResourceReference INSTANCE = new BootstrapAlertJavaScriptResourceReference();

	private BootstrapAlertJavaScriptResourceReference() {
		super(BootstrapAlertJavaScriptResourceReference.class, "alert.js");
	}

	public static BootstrapAlertJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
