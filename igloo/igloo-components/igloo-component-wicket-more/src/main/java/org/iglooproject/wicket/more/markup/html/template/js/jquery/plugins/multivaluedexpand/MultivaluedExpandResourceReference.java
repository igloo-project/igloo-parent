package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.multivaluedexpand;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;

public final class MultivaluedExpandResourceReference extends AbstractCoreJQueryPluginResourceReference {
	
	private static final long serialVersionUID = 4395351510950515587L;
	
	private static final MultivaluedExpandResourceReference INSTANCE = new MultivaluedExpandResourceReference();
	
	private MultivaluedExpandResourceReference() {
		super(MultivaluedExpandResourceReference.class, "jquery.multivaluedExpand.js");
	}
	
	public static MultivaluedExpandResourceReference get() {
		return INSTANCE;
	}
}
