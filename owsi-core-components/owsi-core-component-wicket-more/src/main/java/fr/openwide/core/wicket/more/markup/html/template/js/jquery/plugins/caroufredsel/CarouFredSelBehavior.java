package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.commons.CoreJavaScriptResourceReference;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsStatement;

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
	public void contribute(WiQueryResourceManager wiQueryResourceManager) {
		wiQueryResourceManager.addJavaScriptResource(CoreJavaScriptResourceReference.get());
		wiQueryResourceManager.addJavaScriptResource(EasingJavaScriptResourceReference.get());
		wiQueryResourceManager.addJavaScriptResource(CarouFredSelJavaScriptResourceReference.get());
		super.contribute(wiQueryResourceManager);
	}

	@Override
	public void bind(Component component) {
		component.setOutputMarkupId(true);
		super.bind(component);
	}

}
