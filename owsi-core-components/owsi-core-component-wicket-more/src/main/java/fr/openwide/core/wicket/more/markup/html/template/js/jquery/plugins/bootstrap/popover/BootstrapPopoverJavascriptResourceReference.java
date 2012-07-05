package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipJavascriptResourceReference;

public final class BootstrapPopoverJavascriptResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = 7455873661174214964L;

	private static final BootstrapPopoverJavascriptResourceReference INSTANCE = new BootstrapPopoverJavascriptResourceReference();

	private BootstrapPopoverJavascriptResourceReference() {
		super(BootstrapPopoverJavascriptResourceReference.class, "bootstrap-popover.js");
	}

	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				BootstrapTooltipJavascriptResourceReference.get()
		};
	}

	public static BootstrapPopoverJavascriptResourceReference get() {
		return INSTANCE;
	}

}
