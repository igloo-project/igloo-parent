package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.tooltip;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;
import org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference;

public final class BootstrapTooltipMoreJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final BootstrapTooltipMoreJavaScriptResourceReference INSTANCE = new BootstrapTooltipMoreJavaScriptResourceReference();

	private BootstrapTooltipMoreJavaScriptResourceReference() {
		super(BootstrapTooltipMoreJavaScriptResourceReference.class, "tooltip.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(JavaScriptHeaderItem.forReference(JQueryUIJavaScriptResourceReference.get()));
		dependencies.add(JavaScriptHeaderItem.forReference(BootstrapTooltipJavaScriptResourceReference.get()));
		return dependencies;
	}

	public static BootstrapTooltipMoreJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
