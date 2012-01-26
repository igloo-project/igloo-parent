package fr.openwide.core.wicket.gmap.api.directions;

import java.io.Serializable;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/directions.html"></a>
 */
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
