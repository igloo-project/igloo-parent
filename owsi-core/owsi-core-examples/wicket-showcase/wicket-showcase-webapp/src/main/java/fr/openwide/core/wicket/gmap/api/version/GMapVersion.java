package fr.openwide.core.wicket.gmap.api.version;

/*
 * Google Maps API Versioning
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/basics.html#Versioning"></a>
 */

public enum GMapVersion {

	FROZEN("3.5"),
	FEATURE_STABLE("3.6"),
	CURRENT("3.7");
	
	private String value;
	
	private GMapVersion(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
