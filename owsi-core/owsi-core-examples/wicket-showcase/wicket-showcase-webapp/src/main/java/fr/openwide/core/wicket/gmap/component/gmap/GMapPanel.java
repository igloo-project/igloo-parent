package fr.openwide.core.wicket.gmap.component.gmap;

import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;

import fr.openwide.core.wicket.gmap.component.GMapHeaderContributor;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapOptions;

public class GMapPanel extends Panel {
	private static final long serialVersionUID = -904534558476084988L;
	
	public GMapPanel(String id, GMapOptions options) {
		this(id, null, null, options);
	}
	
	public GMapPanel(String id, Locale region, GMapOptions options) {
		this(id, region, null, null);
	}
	
	public GMapPanel(String id, Locale region, Locale locale, GMapOptions options) {
		super(id);
		
		add(new GMapHeaderContributor(region, locale));
		
		add(new GMapBehavior(options));
	}
	
	public void cleanMarkers() {
		
	}
}