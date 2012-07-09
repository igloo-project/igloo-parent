package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.directions;


/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/directions.html"></a>
 */
public enum GUnitSystem {
	
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
