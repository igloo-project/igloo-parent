package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing;

import org.odlabs.wiquery.core.commons.compressed.WiQueryYUICompressedJavaScriptResourceReference;

public final class EasingJavaScriptResourceReference extends WiQueryYUICompressedJavaScriptResourceReference {
	private static final long serialVersionUID = -7107928462642160157L;
	
	private static final EasingJavaScriptResourceReference INSTANCE = new EasingJavaScriptResourceReference();

	private EasingJavaScriptResourceReference() {
		super(EasingJavaScriptResourceReference.class, "jquery.easing.1.3.js");
	}
	
	public static EasingJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
