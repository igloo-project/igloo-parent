package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.affix;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;


public final class BootstrapAffixJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -5388425553542523759L;

	private static final BootstrapAffixJavaScriptResourceReference INSTANCE = new BootstrapAffixJavaScriptResourceReference();

	private BootstrapAffixJavaScriptResourceReference() {
		super(BootstrapAffixJavaScriptResourceReference.class, "affix.js");
	}

	public static BootstrapAffixJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
