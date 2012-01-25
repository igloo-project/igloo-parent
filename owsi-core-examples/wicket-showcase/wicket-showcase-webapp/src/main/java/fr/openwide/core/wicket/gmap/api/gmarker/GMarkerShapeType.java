package fr.openwide.core.wicket.gmap.api.gmarker;

import java.io.Serializable;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#MarkerShape"></>
 */
public enum GMarkerShapeType implements Serializable {

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
