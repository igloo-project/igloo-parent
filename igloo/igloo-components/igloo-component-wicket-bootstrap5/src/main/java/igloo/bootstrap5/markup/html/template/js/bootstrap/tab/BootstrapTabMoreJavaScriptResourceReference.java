package igloo.bootstrap5.markup.html.template.js.bootstrap.tab;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import igloo.bootstrap5.markup.html.template.js.bootstrap.Bootstrap5JavaScriptResourceReference;

public final class BootstrapTabMoreJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final BootstrapTabMoreJavaScriptResourceReference INSTANCE = new BootstrapTabMoreJavaScriptResourceReference();

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		Bootstrap5JavaScriptResourceReference.get()
	);

	private BootstrapTabMoreJavaScriptResourceReference() {
		super("bootstrap5-override/current/js/dist/tab-more.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static BootstrapTabMoreJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
