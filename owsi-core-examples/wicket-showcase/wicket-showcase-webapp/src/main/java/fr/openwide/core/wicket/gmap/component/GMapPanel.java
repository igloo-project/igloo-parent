package fr.openwide.core.wicket.gmap.component;

import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;

import fr.openwide.core.wicket.gmap.api.GLatLng;
import fr.openwide.core.wicket.gmap.api.GMapTypeId;
import fr.openwide.core.wicket.gmap.api.version.GMapVersion;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapOptions;

public class GMapPanel extends Panel {
	private static final long serialVersionUID = -904534558476084988L;

	private GMapOptions options = new GMapOptions();
	
	public GMapPanel(String id) {
		this(id, null, null);
	}
	
	public GMapPanel(String id, String region, Locale locale) {
		this(id, region, locale, null, null);
	}
	
	public GMapPanel(String id, String region, Locale locale, GMapVersion version) {
		this(id, region, locale, version, null);
	}
	
	public GMapPanel(String id, String region, Locale locale, GMapOptions options) {
		this(id, region, locale, null, options);
	}
	
	public GMapPanel(String id, String region, Locale locale, GMapVersion version, GMapOptions options) {
		super(id);
		
		add(new GMapHeaderContributor(region, locale, version));
		
		if (options == null) {
			createDefaultGMap();
		} else {
			this.options = options;
		}
		
		add(new GMapBehavior(this.options));
	}
	
	private void createDefaultGMap(){
		options.setZoom(1);
		options.setMapTypeId(GMapTypeId.G_ROADMAP_MAP);
		options.setCenter(new GLatLng(-34.397, 150.644));
		options.setDisableDefaultUI(true);  
	}
}
