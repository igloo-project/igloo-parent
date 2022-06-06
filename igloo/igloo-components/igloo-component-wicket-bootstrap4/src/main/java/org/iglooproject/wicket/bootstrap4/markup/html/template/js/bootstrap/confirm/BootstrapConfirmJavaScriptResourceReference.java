package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.confirm;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.Bootstrap4ModalJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.WebjarsJQueryPluginResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

public final class BootstrapConfirmJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		Bootstrap4ModalJavaScriptResourceReference.get()
	);

	private static final BootstrapConfirmJavaScriptResourceReference INSTANCE = new BootstrapConfirmJavaScriptResourceReference();

	private BootstrapConfirmJavaScriptResourceReference() {
		super("bootstrap4-override/current/js/dist/confirm.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static BootstrapConfirmJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
