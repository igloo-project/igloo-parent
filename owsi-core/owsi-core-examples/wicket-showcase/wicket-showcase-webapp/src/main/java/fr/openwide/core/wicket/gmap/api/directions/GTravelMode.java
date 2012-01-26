package fr.openwide.core.wicket.gmap.api.directions;

import java.io.Serializable;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/directions.html"></a>
 */
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
