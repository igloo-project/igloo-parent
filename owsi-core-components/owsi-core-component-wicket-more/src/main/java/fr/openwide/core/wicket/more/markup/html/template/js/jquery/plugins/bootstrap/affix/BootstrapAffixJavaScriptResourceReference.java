package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.affix;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;


public final class BootstrapAffixJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -5388425553542523759L;

	private static final BootstrapAffixJavaScriptResourceReference INSTANCE = new BootstrapAffixJavaScriptResourceReference();

	private BootstrapAffixJavaScriptResourceReference() {
		super(BootstrapAffixJavaScriptResourceReference.class, "bootstrap-affix.js");
	}

	public static BootstrapAffixJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
