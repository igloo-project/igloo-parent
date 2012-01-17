package fr.openwide.core.wicket.gmap.api.gmarker.shape;

import java.io.Serializable;

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
