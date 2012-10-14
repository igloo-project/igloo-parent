package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover;

import org.apache.wicket.markup.head.HeaderItem;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipJavascriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapPopoverJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 7455873661174214964L;

	private static final BootstrapPopoverJavascriptResourceReference INSTANCE = new BootstrapPopoverJavascriptResourceReference();

	private BootstrapPopoverJavascriptResourceReference() {
		super(BootstrapPopoverJavascriptResourceReference.class, "bootstrap-popover.js");
	}

	@Override
	public Iterable<? extends HeaderItem> getPluginDependencies() {
		return JavaScriptHeaderItems.forReferences(BootstrapTooltipJavascriptResourceReference.get());
	}

	public static BootstrapPopoverJavascriptResourceReference get() {
		return INSTANCE;
	}

}
