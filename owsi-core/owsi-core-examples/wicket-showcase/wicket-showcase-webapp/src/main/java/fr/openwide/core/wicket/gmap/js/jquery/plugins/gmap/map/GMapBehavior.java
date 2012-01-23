package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.map;

import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class GMapBehavior extends WiQueryAbstractBehavior {
	private static final long serialVersionUID = 6319723112229959901L;

	private GMapOptions options;
	
	public GMapBehavior(GMapOptions options) {
		this.options = options;
	}
	
	@Override
	public JsStatement statement() {
		return new JsStatement().$(getComponent(), "").chain(options);
	}
}
