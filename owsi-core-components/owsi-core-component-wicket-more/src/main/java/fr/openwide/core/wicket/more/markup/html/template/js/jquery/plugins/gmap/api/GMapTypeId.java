package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api;

import java.util.Locale;

/**
 * Represents an Google Maps API's <a
 * href="http://www.google.com/apis/maps/documentation/reference.html#GMapType">GMapType</a>.
 */
public enum GMapTypeId {
	
	HYBRID("HYBRID"),
	SATELLITE("SATELLITE"),
	ROADMAP("ROADMAP"),
	TERRAIN("TERRAIN");
	
	private String value;
	
	private GMapTypeId(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	private static String normalize(String string) {
		if (string == null) {
			return null;
		}
		return string.toUpperCase(Locale.ROOT);
	}
	
	public static GMapTypeId fromString(String value) {
		for (GMapTypeId type : values()) {
			if (type.getValue().equals(normalize(value))) {
				return type;
			}
		}
		return null;
	}
}