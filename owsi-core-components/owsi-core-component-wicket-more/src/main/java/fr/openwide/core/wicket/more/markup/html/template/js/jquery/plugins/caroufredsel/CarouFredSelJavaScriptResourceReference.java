package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractJQueryPluginResourceReference;

public final class CarouFredSelJavaScriptResourceReference extends AbstractJQueryPluginResourceReference {
	private static final long serialVersionUID = 4911695054626514694L;

	private static final CarouFredSelJavaScriptResourceReference INSTANCE = new CarouFredSelJavaScriptResourceReference();

	private CarouFredSelJavaScriptResourceReference() {
		super(CarouFredSelJavaScriptResourceReference.class, "carouFredSel-5.5.3/jquery.carouFredSel-5.5.3.js");
	}

	@Override
	public ResourceReference[] getInternalDependencies() {
		return new ResourceReference[] { EasingJavaScriptResourceReference.get() };
	}

	public static CarouFredSelJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
