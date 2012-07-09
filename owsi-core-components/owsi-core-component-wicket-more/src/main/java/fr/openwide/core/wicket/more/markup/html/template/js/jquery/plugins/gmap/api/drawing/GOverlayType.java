package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.drawing;


/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#OverlayType"></a>
 */
public enum GOverlayType {
	
	CIRCLE("CIRCLE"),
	MARKER("MARKER"),
	POLYGON("POLYGON"),
	POLYLINE("POLYLINE"),
	RECTANGLE("RECTANGLE");
	
	private String value;
	
	private GOverlayType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
