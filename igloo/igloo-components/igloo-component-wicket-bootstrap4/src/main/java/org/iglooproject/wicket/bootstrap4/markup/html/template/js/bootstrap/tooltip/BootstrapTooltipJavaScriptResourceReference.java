package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.popper.PopperJavaScriptResourceReference;

public final class BootstrapTooltipJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final BootstrapTooltipJavaScriptResourceReference INSTANCE = new BootstrapTooltipJavaScriptResourceReference();

	private BootstrapTooltipJavaScriptResourceReference() {
		super(BootstrapTooltipJavaScriptResourceReference.class, "tooltip-patch.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(
				PopperJavaScriptResourceReference.get(),
				BootstrapUtilJavaScriptResourceReference.get()
		);
	}

	public static BootstrapTooltipJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
