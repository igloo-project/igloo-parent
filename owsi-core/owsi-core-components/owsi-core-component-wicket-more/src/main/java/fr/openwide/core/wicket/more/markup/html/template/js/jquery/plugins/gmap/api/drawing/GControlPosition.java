package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.drawing;


/*
 *	see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#ControlPosition"></>
 *	+----------------+ 
	+ TL    TC    TR + 
	+ LT          RT + 
	+                + 
	+ LC          RC + 
	+                + 
	+ LB          RB + 
	+ BL    BC    BR + 
	+----------------+ 
 */
public enum GControlPosition {

	BOTTOM_CENTER("BOTTOM_CENTER"),
	BOTTOM_LEFT("BOTTOM_LEFT"),
	BOTTOM_RIGHT("BOTTOM_RIGHT"),
	LEFT_BOTTOM("LEFT_BOTTOM"),
	LEFT_CENTER("LEFT_CENTER"),
	LEFT_TOP("LEFT_TOP"),
	RIGHT_BOTTOM("RIGHT_BOTTOM"),
	RIGHT_CENTER("RIGHT_CENTER"),
	RIGHT_TOP("RIGHT_TOP"),
	TOP_CENTER("TOP_CENTER"),
	TOP_LEFT("TOP_LEFT"),
	TOP_RIGHT("	TOP_RIGHT");
	
	
	private String value;
	
	private GControlPosition(String value) {
		this.value =value;
	}
	
	public String getValue() {
		return value;
	}
}
