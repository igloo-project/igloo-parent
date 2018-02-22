package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstraphoverdropdown;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapHoverDropdownJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	
	private static final long serialVersionUID = 3363577450073384493L;
	
	private static final BootstrapHoverDropdownJavaScriptResourceReference INSTANCE = new BootstrapHoverDropdownJavaScriptResourceReference();
	
	private BootstrapHoverDropdownJavaScriptResourceReference() {
		super(BootstrapHoverDropdownJavaScriptResourceReference.class, "bootstrap-hover-dropdown.js");
	}
	
	public static BootstrapHoverDropdownJavaScriptResourceReference get() {
		return INSTANCE;
	}
}
