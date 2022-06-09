package igloo.bootstrap4.markup.html.template.js.bootstrap.scrollspy;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

import igloo.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import igloo.jquery.util.WebjarsJQueryPluginResourceReference;

/**
 * @deprecated see {@link IBootstrapScrollSpy}
 */
@Deprecated(since = "4.3.0")
public final class BootstrapScrollSpyJavaScriptResourceReference extends WebjarsJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		BootstrapUtilJavaScriptResourceReference.get()
	);

	private static final BootstrapScrollSpyJavaScriptResourceReference INSTANCE = new BootstrapScrollSpyJavaScriptResourceReference();

	private BootstrapScrollSpyJavaScriptResourceReference() {
		super("bootstrap4/current/js/dist/scrollspy.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static BootstrapScrollSpyJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
