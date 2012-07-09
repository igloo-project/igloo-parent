package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrollinviewport;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class ScrollInViewportBehavior extends WiQueryAbstractBehavior {

	private static final long serialVersionUID = -6068559743555922420L;

	@Override
	public JsStatement statement() {
		return new JsQuery(getComponent()).$().chain("scrollInViewport");
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		response.renderJavaScriptReference(ScrollInViewportResourceReference.get());
	}

}
