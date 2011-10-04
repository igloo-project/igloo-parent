package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

import org.odlabs.wiquery.core.commons.WiQueryJavaScriptResourceReference;

public class CarouFredSelJavaScriptResourceReference extends
		WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = 4911695054626514694L;

	private static final CarouFredSelJavaScriptResourceReference INSTANCE = new CarouFredSelJavaScriptResourceReference();

	public CarouFredSelJavaScriptResourceReference() {
		super(CarouFredSelJavaScriptResourceReference.class, "carouFredSel-3.1.0/jquery.carouFredSel-3.1.0.js");
	}
	
	public static CarouFredSelJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
