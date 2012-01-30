package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing;

import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class GDrawingManagerBehavior extends WiQueryAbstractBehavior {
	private static final long serialVersionUID = 7050262144439397070L;

	private GDrawingManagerOptions options;
	
	public GDrawingManagerBehavior(GDrawingManagerOptions options) {
		this.options = options;
	}
	
	@Override
	public JsStatement statement() {
		return new JsStatement().$(options.getMap(), "").chain(options);
	}
	

}
