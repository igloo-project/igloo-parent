package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.infowindow;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

/*
 * Infobubble version 1.1.11
 * see <a href="http://code.google.com/p/google-maps-utility-library-v3/"></a>
 */
public class InfoBubbleResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = -9125098024654674764L;
	
	private static final InfoBubbleResourceReference INSTANCE = new InfoBubbleResourceReference();
	
	private InfoBubbleResourceReference() {
		super(InfoBubbleResourceReference.class, "infobubble.js");
	}
	
	public static final InfoBubbleResourceReference get() {
		return INSTANCE;
	}
}
