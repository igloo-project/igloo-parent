package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.dropdown;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;


public final class BootstrapDropDownJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -5388425553542523759L;

	private static final BootstrapDropDownJavaScriptResourceReference INSTANCE = new BootstrapDropDownJavaScriptResourceReference();

	private BootstrapDropDownJavaScriptResourceReference() {
		super(BootstrapDropDownJavaScriptResourceReference.class, "dropdown.js");
	}

	public static BootstrapDropDownJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
