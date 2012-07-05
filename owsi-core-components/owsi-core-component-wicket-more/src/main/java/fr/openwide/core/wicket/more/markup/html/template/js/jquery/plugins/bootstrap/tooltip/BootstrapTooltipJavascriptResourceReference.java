package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public final class BootstrapTooltipJavascriptResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final BootstrapTooltipJavascriptResourceReference INSTANCE = new BootstrapTooltipJavascriptResourceReference();

	private BootstrapTooltipJavascriptResourceReference() {
		super(BootstrapTooltipJavascriptResourceReference.class, "bootstrap-tooltip.js");
	}

	public static BootstrapTooltipJavascriptResourceReference get() {
		return INSTANCE;
	}

}
