package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

import org.odlabs.wiquery.core.javascript.JsStatement;

public final class TipsyHelper {
	
	public static final JsStatement closeTipsyStatement() {
		return new JsStatement().$(null, ".tipsy").chain("detach");
	}
	
	private TipsyHelper() {
	}

}
