package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
public final class BootstrapTooltipJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final BootstrapTooltipJavascriptResourceReference INSTANCE = new BootstrapTooltipJavascriptResourceReference();

	private BootstrapTooltipJavascriptResourceReference() {
		super(BootstrapTooltipJavascriptResourceReference.class, "tooltip.js");
	}

	public static BootstrapTooltipJavascriptResourceReference get() {
		return INSTANCE;
	}

}
