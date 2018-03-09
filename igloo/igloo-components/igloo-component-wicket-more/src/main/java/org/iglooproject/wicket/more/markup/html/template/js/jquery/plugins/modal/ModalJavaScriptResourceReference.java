package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.modal;

import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop.ScrollToTopJavaScriptResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;
import org.wicketstuff.wiquery.ui.mouse.MouseJavaScriptResourceReference;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

/**
 * @deprecated Fancybox is no longer maintained; this component needs to be replaced. Backward compatibility will
 * not be enforced.
 */
@Deprecated
public final class ModalJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;

	private static final Supplier<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
			EasingJavaScriptResourceReference.get(),
			MouseJavaScriptResourceReference.get(),
			ScrollToTopJavaScriptResourceReference.get(),
			ModalStyleSheetResourceReference.get()
	);
	
	private static final ModalJavaScriptResourceReference INSTANCE = new ModalJavaScriptResourceReference();

	private ModalJavaScriptResourceReference() {
		super("jquery.fancybox/current/jquery.fancybox-1.3.5-ow.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return DEPENDENCIES.get();
	}

	public static ModalJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
