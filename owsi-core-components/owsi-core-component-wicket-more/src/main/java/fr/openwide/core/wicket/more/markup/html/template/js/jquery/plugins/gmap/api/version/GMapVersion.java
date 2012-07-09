package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.version;

/*
 * Google Maps API Versioning
 * see <a href="https://developers.google.com/maps/documentation/javascript/basics?hl=fr-FR#Versioning"></a>
 */

public enum GMapVersion {

	RELEASE("3.8"),
	NIGHTLY("3.9");
	
	private String value;
	
	private GMapVersion(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
