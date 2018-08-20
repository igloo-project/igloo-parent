package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.popover;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.tooltip.BootstrapTooltipJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapPopoverJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 7455873661174214964L;

	private static final BootstrapPopoverJavaScriptResourceReference INSTANCE = new BootstrapPopoverJavaScriptResourceReference();

	private BootstrapPopoverJavaScriptResourceReference() {
		super(BootstrapPopoverJavaScriptResourceReference.class, "popover.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(BootstrapTooltipJavaScriptResourceReference.get());
	}

	public static BootstrapPopoverJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
