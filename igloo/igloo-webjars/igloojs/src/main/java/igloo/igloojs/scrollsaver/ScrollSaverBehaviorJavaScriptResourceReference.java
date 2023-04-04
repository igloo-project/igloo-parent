package igloo.igloojs.scrollsaver;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import igloo.igloojs.lodash.LodashJavaScriptResourceReference;

public class ScrollSaverBehaviorJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = 2306073343522529577L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = Suppliers2.memoize(() -> List.of(
		JavaScriptHeaderItem.forReference(LodashJavaScriptResourceReference.get())
	));

	private static final ScrollSaverBehaviorJavaScriptResourceReference INSTANCE = new ScrollSaverBehaviorJavaScriptResourceReference();

	public static ScrollSaverBehaviorJavaScriptResourceReference get() {
		return INSTANCE;
	}

	public ScrollSaverBehaviorJavaScriptResourceReference() {
		super("igloojs/current/dist/scroll-saver.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

}
