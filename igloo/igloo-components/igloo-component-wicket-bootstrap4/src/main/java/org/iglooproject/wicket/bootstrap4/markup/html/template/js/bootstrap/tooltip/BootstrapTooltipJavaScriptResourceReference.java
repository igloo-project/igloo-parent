package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.bootstrap.api.popper.PopperJavaScriptResourceReference;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsJQueryPluginResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

public final class BootstrapTooltipJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		PopperJavaScriptResourceReference.get(),
		BootstrapUtilJavaScriptResourceReference.get()
	);

	private static final BootstrapTooltipJavaScriptResourceReference INSTANCE = new BootstrapTooltipJavaScriptResourceReference();

	private BootstrapTooltipJavaScriptResourceReference() {
		super("bootstrap4-override/current/js/dist/tooltip.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static BootstrapTooltipJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
