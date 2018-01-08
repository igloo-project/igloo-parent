package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.button;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
public final class BootstrapButtonJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	
	private static final long serialVersionUID = 2483430588472990612L;
	
	private static final BootstrapButtonJavaScriptResourceReference INSTANCE = new BootstrapButtonJavaScriptResourceReference();
	
	private BootstrapButtonJavaScriptResourceReference() {
		super(BootstrapButtonJavaScriptResourceReference.class, "button.js");
	}
	
	public static BootstrapButtonJavaScriptResourceReference get() {
		return INSTANCE;
	}
}
