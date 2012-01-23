package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.infoWindow;

import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class GInfoBubbleBehavior extends WiQueryAbstractBehavior {
	private static final long serialVersionUID = 6319723112229959901L;

	private GInfoBubbleOptions options;
	
	public GInfoBubbleBehavior(GInfoBubbleOptions options) {
		this.options = options;
	}
	
	@Override
	public JsStatement statement() {
		return new JsStatement().$(options.getMap(), "").chain(options);
	}
}