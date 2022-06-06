package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.button;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsJQueryPluginResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

public final class BootstrapButtonJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		BootstrapUtilJavaScriptResourceReference.get()
	);

	private static final BootstrapButtonJavaScriptResourceReference INSTANCE = new BootstrapButtonJavaScriptResourceReference();

	private BootstrapButtonJavaScriptResourceReference() {
		super("bootstrap4/current/js/dist/button.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static BootstrapButtonJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
