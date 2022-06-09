package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.mask;

import igloo.jquery.util.WebjarsJQueryPluginResourceReference;

public final class MaskJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -3780299775263409665L;

	private static final MaskJavaScriptResourceReference INSTANCE = new MaskJavaScriptResourceReference();

	private MaskJavaScriptResourceReference() {
		super("jquery-mask-plugin/current/dist/jquery.mask.js");
	}

	public static MaskJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
