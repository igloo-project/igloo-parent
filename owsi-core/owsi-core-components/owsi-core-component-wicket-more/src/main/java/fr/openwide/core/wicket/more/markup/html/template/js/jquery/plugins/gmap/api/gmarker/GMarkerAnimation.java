package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.gmarker;


/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#Animation"></a>
 */
public enum GMarkerAnimation {
	
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
