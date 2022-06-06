package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.bootstrap.api.popper.PopperJavaScriptResourceReference;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class Bootstrap5JavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		PopperJavaScriptResourceReference.get()
	);

	private static final Bootstrap5JavaScriptResourceReference INSTANCE = new Bootstrap5JavaScriptResourceReference();

	private Bootstrap5JavaScriptResourceReference() {
		super("bootstrap/current/dist/js/bootstrap.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static Bootstrap5JavaScriptResourceReference get() {
		return INSTANCE;
	}

}
