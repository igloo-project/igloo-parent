package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.elastic;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryAbstractBehavior;

public class ElasticBehavior extends JQueryAbstractBehavior {

	private static final long serialVersionUID = -8169552850152371154L;

	public JsStatement statement() {
		if (getComponent() != null) {
			return new JsStatement().$(getComponent()).chain("elastic");
		} else {
			return null;
		}
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(ElasticJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement().render()));
	}

}
