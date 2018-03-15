package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.popover;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip.BootstrapTooltipJavaScriptResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class BootstrapPopoverJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
			BootstrapTooltipJavaScriptResourceReference.get()
	);
	
	private static final BootstrapPopoverJavaScriptResourceReference INSTANCE = new BootstrapPopoverJavaScriptResourceReference();

	private BootstrapPopoverJavaScriptResourceReference() {
		super("bootstrap/current/js/dist/popover.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return DEPENDENCIES.get();
	}

	public static BootstrapPopoverJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
