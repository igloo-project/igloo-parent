package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap4.tooltip;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap4.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.popper.PopperJavaScriptResourceReference;

public final class BootstrapTooltipJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final BootstrapTooltipJavascriptResourceReference INSTANCE = new BootstrapTooltipJavascriptResourceReference();

	private BootstrapTooltipJavascriptResourceReference() {
		super(BootstrapTooltipJavascriptResourceReference.class, "tooltip.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(
				PopperJavaScriptResourceReference.get(),
				BootstrapUtilJavaScriptResourceReference.get()
		);
	}

	public static BootstrapTooltipJavascriptResourceReference get() {
		return INSTANCE;
	}

}
