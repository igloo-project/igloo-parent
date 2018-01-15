package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.tab;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapTabJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 4895439022427021364L;

	private static final BootstrapTabJavaScriptResourceReference INSTANCE = new BootstrapTabJavaScriptResourceReference();

	private BootstrapTabJavaScriptResourceReference() {
		super(BootstrapTabJavaScriptResourceReference.class, "tab.js");
	}

	public static BootstrapTabJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
