package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.commons.CoreJavaScriptResourceReference;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsStatement;

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
	public void contribute(WiQueryResourceManager wiQueryResourceManager) {
		wiQueryResourceManager.addJavaScriptResource(CoreJavaScriptResourceReference.get());
		wiQueryResourceManager.addJavaScriptResource(TipsyJavascriptResourceReference.get());
		super.contribute(wiQueryResourceManager);
	}

	@Override
	public JsStatement statement() {
		return new JsStatement().$(null, selector).chain(tipsy);
	}

}
