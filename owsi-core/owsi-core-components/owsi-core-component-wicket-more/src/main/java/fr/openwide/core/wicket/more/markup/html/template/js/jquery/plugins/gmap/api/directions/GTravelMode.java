package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.directions;


/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/directions.html"></a>
 */
public enum GTravelMode {

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
