package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tab;

import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class BootstrapTabJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final Supplier<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
			BootstrapUtilJavaScriptResourceReference.get()
	);
	
	private static final BootstrapTabJavaScriptResourceReference INSTANCE = new BootstrapTabJavaScriptResourceReference();

	private BootstrapTabJavaScriptResourceReference() {
		super("bootstrap/current/js/dist/tab.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return DEPENDENCIES.get();
	}

	public static BootstrapTabJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
