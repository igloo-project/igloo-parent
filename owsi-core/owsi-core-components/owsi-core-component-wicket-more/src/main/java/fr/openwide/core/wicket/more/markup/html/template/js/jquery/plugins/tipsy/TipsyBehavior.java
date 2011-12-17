package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;

public class TipsyBehavior extends WiQueryAbstractBehavior {
	private static final long serialVersionUID = 6319723112229959901L;
	
	private String selector;
	
	private Tipsy tipsy;
	
	public TipsyBehavior(String selector, Tipsy tipsy) {
		super();
		this.selector = selector;
		this.tipsy = tipsy;
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
		response.renderJavaScriptReference(TipsyJavascriptResourceReference.get());
	}

	@Override
	public JsStatement statement() {
		return new JsStatement().$(null, selector).chain(tipsy);
	}

}
