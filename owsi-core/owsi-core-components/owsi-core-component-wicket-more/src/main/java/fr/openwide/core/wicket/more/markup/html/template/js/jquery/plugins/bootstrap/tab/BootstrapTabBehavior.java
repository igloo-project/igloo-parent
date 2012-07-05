package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tab;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class BootstrapTabBehavior extends WiQueryAbstractBehavior {
	
	private static final long serialVersionUID = 1645525017124363380L;

	@Override
	public JsStatement statement() {
		return new JsStatement().$(getComponent()).chain(new BootstrapTab());
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(BootstrapTabJavascriptResourceReference.get());
	}

}