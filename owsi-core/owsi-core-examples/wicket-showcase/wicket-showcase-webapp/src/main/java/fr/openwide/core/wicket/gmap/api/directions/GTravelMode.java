package fr.openwide.core.wicket.gmap.api.directions;

import java.io.Serializable;

public enum GTravelMode implements Serializable {

	DRIVING("DRIVING"),
	WALKING("WALKING"),
	BICYCLING("BICYCLING");
	
	private String value;
	
	private GTravelMode(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
