package fr.openwide.core.wicket.gmap.component;

import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;

import fr.openwide.core.wicket.gmap.GMapHeaderContributor;
import fr.openwide.core.wicket.gmap.api.GLatLng;
import fr.openwide.core.wicket.gmap.api.GMapTypeId;
import fr.openwide.core.wicket.gmap.api.version.GMapVersion;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapOptions;

public class GMapPanel extends Panel {
	private static final long serialVersionUID = -904534558476084988L;

	private GMapOptions options;
	
	public GMapPanel(String id) {
		super(id);
		
		add(new GMapHeaderContributor("FR", Locale.FRANCE, GMapVersion.CURRENT));
		
		options = new GMapOptions();
		options.setZoom(3);
		options.setMapTypeId(GMapTypeId.G_ROADMAP_MAP);
		options.setCenter(new GLatLng(-34.397, 150.644));
		
		add(new GMapBehavior(options));
	}
}
