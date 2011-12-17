package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;

public class CarouFredSelBehavior extends WiQueryAbstractBehavior {
	private static final long serialVersionUID = 6155882407495564466L;
	
	private CarouFredSel carouFredSel;
	
	public CarouFredSelBehavior(CarouFredSel carouFredSel) {
		super();
		this.carouFredSel = carouFredSel;
	}

	@Override
	public JsStatement statement() {
		return new JsStatement().$(getComponent()).chain(carouFredSel);
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
		response.renderJavaScriptReference(EasingJavaScriptResourceReference.get());
		response.renderJavaScriptReference(CarouFredSelJavaScriptResourceReference.get());
	}

}
