package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsJQueryPluginResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

public final class BootstrapModalMoreJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		BootstrapModalJavaScriptResourceReference.get()
	);

	private static final BootstrapModalMoreJavaScriptResourceReference INSTANCE = new BootstrapModalMoreJavaScriptResourceReference();

	private BootstrapModalMoreJavaScriptResourceReference() {
		super("webjars/bootstrap4-override/current/js/dist/modal-more.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static BootstrapModalMoreJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
