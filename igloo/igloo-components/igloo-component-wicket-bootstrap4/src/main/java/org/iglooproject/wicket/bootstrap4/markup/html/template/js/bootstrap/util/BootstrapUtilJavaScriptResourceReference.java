package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapUtilJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;
	
	private static final BootstrapUtilJavaScriptResourceReference INSTANCE = new BootstrapUtilJavaScriptResourceReference();

	private BootstrapUtilJavaScriptResourceReference() {
		super(BootstrapUtilJavaScriptResourceReference.class, "util.js");
	}

	public static BootstrapUtilJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
