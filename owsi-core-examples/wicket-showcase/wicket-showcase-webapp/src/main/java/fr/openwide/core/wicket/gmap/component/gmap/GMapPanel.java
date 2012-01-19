package fr.openwide.core.wicket.gmap.component.gmap;

import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.gmap.api.GLatLng;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerAnimation;
import fr.openwide.core.wicket.gmap.component.GMapHeaderContributor;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.action.GMapChainableStatement;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerOptions;

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
		
		// Marker Create On Map Event
		final GMarkerOptions markerOptions = new GMarkerOptions("createOnEvent", new GLatLng(-34.397, 150.644), this);
		markerOptions.setAnimation(GMarkerAnimation.BOUNCE);
		markerOptions.setDraggable(false);
		
		// behavior pour la génération de nouveau marker (ajouter avant le behavior init)
		add(new WiQueryAbstractBehavior(){
			private static final long serialVersionUID = 3947112387454820398L;

			@Override
			public JsStatement statement() {
				return new JsStatement().$(GMapPanel.this, "")
						.chain(new GMapChainableStatement.AddMarkerOnEventChainableStatement(markerOptions, "rightclick"));
			}
		});
		add(new GMapBehavior(options));
	}
	
	public void cleanMarkers() {
		
	}
}