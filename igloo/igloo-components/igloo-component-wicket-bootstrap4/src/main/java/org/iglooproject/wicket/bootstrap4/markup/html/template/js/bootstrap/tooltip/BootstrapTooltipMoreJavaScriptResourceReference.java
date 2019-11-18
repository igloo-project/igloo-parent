package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsJQueryPluginResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

public final class BootstrapTooltipMoreJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		BootstrapTooltipJavaScriptResourceReference.get()
	);

	private static final BootstrapTooltipMoreJavaScriptResourceReference INSTANCE = new BootstrapTooltipMoreJavaScriptResourceReference();

	private BootstrapTooltipMoreJavaScriptResourceReference() {
		super("bootstrap-override/current/js/dist/tooltip-more.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static BootstrapTooltipMoreJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
