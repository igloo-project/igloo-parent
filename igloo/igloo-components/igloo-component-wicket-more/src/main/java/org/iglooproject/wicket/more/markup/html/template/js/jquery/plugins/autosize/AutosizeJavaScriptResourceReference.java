package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.autosize;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class AutosizeJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -467593019533386890L;

	private static final AutosizeJavaScriptResourceReference INSTANCE = new AutosizeJavaScriptResourceReference();

	private AutosizeJavaScriptResourceReference() {
		super(AutosizeJavaScriptResourceReference.class, "jquery.autosize.js");
	}

	public static AutosizeJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
