package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.popper.PopperJavaScriptResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class BootstrapTooltipJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
			PopperJavaScriptResourceReference.get(),
			BootstrapUtilJavaScriptResourceReference.get()
	);

	private static final BootstrapTooltipJavaScriptResourceReference INSTANCE = new BootstrapTooltipJavaScriptResourceReference();

	private BootstrapTooltipJavaScriptResourceReference() {
		super("bootstrap-override/current/js/dist/tooltip.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return DEPENDENCIES.get();
	}

	public static BootstrapTooltipJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
