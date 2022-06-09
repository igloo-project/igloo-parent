package igloo.bootstrap4.markup.html.template.js.bootstrap.modal;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

import igloo.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import igloo.jquery.util.WebjarsJQueryPluginResourceReference;

public final class Bootstrap4ModalJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -8799742276479282371L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		BootstrapUtilJavaScriptResourceReference.get()
	);

	private static final Bootstrap4ModalJavaScriptResourceReference INSTANCE = new Bootstrap4ModalJavaScriptResourceReference();

	private Bootstrap4ModalJavaScriptResourceReference() {
		super("bootstrap4/current/js/dist/modal.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static Bootstrap4ModalJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
