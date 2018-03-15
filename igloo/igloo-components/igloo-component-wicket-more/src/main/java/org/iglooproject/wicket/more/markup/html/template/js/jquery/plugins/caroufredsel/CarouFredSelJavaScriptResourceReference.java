package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

/**
 * CarouFredSel is no longer maintained; this component needs to be replaced. Backward compatibility will not be
 * enforced.
 */
@Deprecated
public final class CarouFredSelJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {
	private static final long serialVersionUID = 4911695054626514694L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
			EasingJavaScriptResourceReference.get());

	private static final CarouFredSelJavaScriptResourceReference INSTANCE = new CarouFredSelJavaScriptResourceReference();

	private CarouFredSelJavaScriptResourceReference() {
		super("jquery.carouFredSel/current/jquery.carouFredSel-6.1.0.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return DEPENDENCIES.get();
	}

	public static CarouFredSelJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
