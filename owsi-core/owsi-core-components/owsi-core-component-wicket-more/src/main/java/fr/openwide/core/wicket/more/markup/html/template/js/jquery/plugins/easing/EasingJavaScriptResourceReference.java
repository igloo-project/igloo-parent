package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;


public final class EasingJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = -7107928462642160157L;
	
	private static final EasingJavaScriptResourceReference INSTANCE = new EasingJavaScriptResourceReference();

	private EasingJavaScriptResourceReference() {
		super(EasingJavaScriptResourceReference.class, "jquery.easing.1.3.js");
	}

	public static EasingJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
