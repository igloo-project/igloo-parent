package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.tooltip;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapTooltipJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final BootstrapTooltipJavaScriptResourceReference INSTANCE = new BootstrapTooltipJavaScriptResourceReference();

	private BootstrapTooltipJavaScriptResourceReference() {
		super(BootstrapTooltipJavaScriptResourceReference.class, "tooltip.js");
	}

	public static BootstrapTooltipJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
