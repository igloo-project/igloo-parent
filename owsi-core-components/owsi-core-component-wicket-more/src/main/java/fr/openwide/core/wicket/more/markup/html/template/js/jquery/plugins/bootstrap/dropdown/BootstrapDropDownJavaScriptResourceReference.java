package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.dropdown;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;


public final class BootstrapDropDownJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -5388425553542523759L;

	private static final BootstrapDropDownJavaScriptResourceReference INSTANCE = new BootstrapDropDownJavaScriptResourceReference();

	private BootstrapDropDownJavaScriptResourceReference() {
		super(BootstrapDropDownJavaScriptResourceReference.class, "bootstrap-dropdown.js");
	}

	public static BootstrapDropDownJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
