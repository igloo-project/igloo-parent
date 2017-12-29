package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.scrollspy;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapScrollSpyJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1777703340106781128L;
	
	private static final BootstrapScrollSpyJavaScriptResourceReference INSTANCE = new BootstrapScrollSpyJavaScriptResourceReference();

	private BootstrapScrollSpyJavaScriptResourceReference() {
		super(BootstrapScrollSpyJavaScriptResourceReference.class, "scrollspy.js");
	}

	public static BootstrapScrollSpyJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
