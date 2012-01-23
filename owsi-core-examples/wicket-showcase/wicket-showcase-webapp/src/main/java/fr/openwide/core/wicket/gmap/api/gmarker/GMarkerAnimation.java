package fr.openwide.core.wicket.gmap.api.gmarker;

import java.io.Serializable;

public enum GMarkerAnimation implements Serializable {
	
	BOUNCE("BOUNCE"),
	DROP("DROP");
	
	private String value;

	private GMarkerAnimation(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
