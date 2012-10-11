package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover;

import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipJavascriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryJavaScriptResourceReference;

public final class BootstrapPopoverJavascriptResourceReference extends JQueryJavaScriptResourceReference {

	private static final long serialVersionUID = 7455873661174214964L;

	private static final BootstrapPopoverJavascriptResourceReference INSTANCE = new BootstrapPopoverJavascriptResourceReference();

	private BootstrapPopoverJavascriptResourceReference() {
		super(BootstrapPopoverJavascriptResourceReference.class, "bootstrap-popover.js");
	}

	@Override
	public ResourceReference[] getInternalDependencies() {
		return new ResourceReference[] { BootstrapTooltipJavascriptResourceReference.get() };
	}

	public static BootstrapPopoverJavascriptResourceReference get() {
		return INSTANCE;
	}

}
