package fr.openwide.core.wicket.gmap.api.directions;

import java.io.Serializable;

public enum GUnitSystem implements Serializable {
	
	METRIC("METRIC"),
	IMPERIAL("IMPERIAL");
	
	private String value;
	
	private GUnitSystem(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
