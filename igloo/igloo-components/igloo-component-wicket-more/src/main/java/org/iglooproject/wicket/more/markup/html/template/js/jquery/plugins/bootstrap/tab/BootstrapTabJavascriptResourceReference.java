package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tab;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
public final class BootstrapTabJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 4895439022427021364L;

	private static final BootstrapTabJavascriptResourceReference INSTANCE = new BootstrapTabJavascriptResourceReference();

	private BootstrapTabJavascriptResourceReference() {
		super(BootstrapTabJavascriptResourceReference.class, "tab.js");
	}

	public static BootstrapTabJavascriptResourceReference get() {
		return INSTANCE;
	}

}
