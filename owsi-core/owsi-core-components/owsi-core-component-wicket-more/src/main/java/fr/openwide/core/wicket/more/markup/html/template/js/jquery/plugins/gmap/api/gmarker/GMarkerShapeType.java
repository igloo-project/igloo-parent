package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.gmarker;


/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#MarkerShape"></>
 */
public enum GMarkerShapeType {

	RECTANGLE("rect"),
	POLY("poly"),
	CIRCLE("CIRCLE");
	
	private String value;
	
	private GMarkerShapeType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
