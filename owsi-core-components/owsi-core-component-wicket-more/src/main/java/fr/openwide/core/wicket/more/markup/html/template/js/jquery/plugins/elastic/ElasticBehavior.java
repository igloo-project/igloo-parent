package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.elastic;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class ElasticBehavior extends WiQueryAbstractBehavior {

	private static final long serialVersionUID = -8169552850152371154L;

	@Override
	public JsStatement statement() {
		if (getComponent() != null) {
			return new JsStatement().$(getComponent()).chain("elastic");
		} else {
			return null;
		}
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(ElasticJavaScriptResourceReference.get());
	}

}
