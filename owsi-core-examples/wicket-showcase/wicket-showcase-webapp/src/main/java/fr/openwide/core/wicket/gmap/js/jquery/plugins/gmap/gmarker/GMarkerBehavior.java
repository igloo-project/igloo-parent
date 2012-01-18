package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker;

import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;


public class GMarkerBehavior extends WiQueryAbstractBehavior {
	private static final long serialVersionUID = 6319723112229959901L;

	private GMarkerOptions options;
	
	public GMarkerBehavior(GMarkerOptions options) {
		this.options = options;
	}
	
	@Override
	public JsStatement statement() {
		return new JsStatement().$(options.getMap(), "").chain(options);
	}
}
