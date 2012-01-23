package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.wicket;

import org.apache.wicket.markup.html.WicketEventReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public class WicketEventResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = -3630476829776745509L;
	
	/**
	 * Singleton instance of this reference
	 */
	public static final WiQueryJavaScriptResourceReference INSTANCE = new WicketEventResourceReference();

	private WicketEventResourceReference() {
		super(WicketEventReference.class, "wicket-event.js");
	}
}
