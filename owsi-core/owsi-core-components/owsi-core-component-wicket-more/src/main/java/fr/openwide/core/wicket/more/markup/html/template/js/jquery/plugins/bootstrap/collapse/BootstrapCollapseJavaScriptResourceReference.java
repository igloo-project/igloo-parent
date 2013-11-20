package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.collapse;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;


public final class BootstrapCollapseJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -5388425553542523759L;

	private static final BootstrapCollapseJavaScriptResourceReference INSTANCE = new BootstrapCollapseJavaScriptResourceReference();

	private BootstrapCollapseJavaScriptResourceReference() {
		super(BootstrapCollapseJavaScriptResourceReference.class, "bootstrap-collapse.js");
	}

	public static BootstrapCollapseJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
