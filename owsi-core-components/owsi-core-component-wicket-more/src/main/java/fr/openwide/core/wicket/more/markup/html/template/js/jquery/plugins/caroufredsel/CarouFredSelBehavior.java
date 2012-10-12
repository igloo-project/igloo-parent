package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.caroufredsel;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class CarouFredSelBehavior extends Behavior {

	private static final long serialVersionUID = 6155882407495564466L;

	private CarouFredSel carouFredSel;

	public CarouFredSelBehavior(CarouFredSel carouFredSel) {
		super();
		this.carouFredSel = carouFredSel;
	}

	public JsStatement statement(Component component) {
		JsScope scope = JsScope.quickScope(new JsStatement().$(component).chain(carouFredSel));
		return new JsStatement().append("$(window).load(").append(scope.render()).append(")");
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(CarouFredSelJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement(component).render()));
	}

}
