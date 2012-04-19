package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public final class CarouFredSelJavaScriptResourceReference extends
		WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = 4911695054626514694L;

	private static final CarouFredSelJavaScriptResourceReference INSTANCE = new CarouFredSelJavaScriptResourceReference();

	private CarouFredSelJavaScriptResourceReference() {
		super(CarouFredSelJavaScriptResourceReference.class, "carouFredSel-5.5.3/jquery.carouFredSel-5.5.3.js");
	}
	
	public static CarouFredSelJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
