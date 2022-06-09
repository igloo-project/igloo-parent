package igloo.bootstrap4.markup.html.template.js.bootstrap.alert;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

import igloo.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import igloo.jquery.util.WebjarsJQueryPluginResourceReference;

public final class BootstrapAlertJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		BootstrapUtilJavaScriptResourceReference.get()
	);

	private static final BootstrapAlertJavaScriptResourceReference INSTANCE = new BootstrapAlertJavaScriptResourceReference();

	private BootstrapAlertJavaScriptResourceReference() {
		super("bootstrap4/current/js/dist/alert.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static BootstrapAlertJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
