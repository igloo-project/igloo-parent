package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.popover;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap3.tooltip.BootstrapTooltipJavascriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
public final class BootstrapPopoverJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 7455873661174214964L;

	private static final BootstrapPopoverJavascriptResourceReference INSTANCE = new BootstrapPopoverJavascriptResourceReference();

	private BootstrapPopoverJavascriptResourceReference() {
		super(BootstrapPopoverJavascriptResourceReference.class, "popover.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(BootstrapTooltipJavascriptResourceReference.get());
	}

	public static BootstrapPopoverJavascriptResourceReference get() {
		return INSTANCE;
	}

}
