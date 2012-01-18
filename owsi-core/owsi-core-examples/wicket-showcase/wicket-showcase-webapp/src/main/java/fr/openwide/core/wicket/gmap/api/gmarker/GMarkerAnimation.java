package fr.openwide.core.wicket.gmap.api.gmarker;

import fr.openwide.core.wicket.gmap.api.GValue;

public enum GMarkerAnimation implements GValue {
	
	BOUNCE("BOUNCE"),
	DROP("DROP");
	
	private String value;

	private GMarkerAnimation(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String getJavaScriptStatement() {
		return "google.maps.Animation." + value;
	}
}
