package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

import org.apache.wicket.markup.head.HeaderItem;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class CarouFredSelJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = 4911695054626514694L;

	private static final CarouFredSelJavaScriptResourceReference INSTANCE = new CarouFredSelJavaScriptResourceReference();

	private CarouFredSelJavaScriptResourceReference() {
		super(CarouFredSelJavaScriptResourceReference.class, "carouFredSel-6.1.0/jquery.carouFredSel-6.1.0.js");
	}

	@Override
	protected Iterable<? extends HeaderItem> getPluginDependencies() {
		return JavaScriptHeaderItems.forReferences(EasingJavaScriptResourceReference.get());
	}

	public static CarouFredSelJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
