package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.mask;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class MaskJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	
	private static final long serialVersionUID = -3780299775263409665L;
	
	private static final MaskJavaScriptResourceReference INSTANCE = new MaskJavaScriptResourceReference();
	
	private MaskJavaScriptResourceReference() {
		super(MaskJavaScriptResourceReference.class, "jquery.mask.js");
	}
	
	public static MaskJavaScriptResourceReference get() {
		return INSTANCE;
	}
}
