package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.collapse;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
public final class BootstrapCollapseJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -5388425553542523759L;

	private static final BootstrapCollapseJavaScriptResourceReference INSTANCE = new BootstrapCollapseJavaScriptResourceReference();

	private BootstrapCollapseJavaScriptResourceReference() {
		super(BootstrapCollapseJavaScriptResourceReference.class, "collapse.js");
	}

	public static BootstrapCollapseJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
