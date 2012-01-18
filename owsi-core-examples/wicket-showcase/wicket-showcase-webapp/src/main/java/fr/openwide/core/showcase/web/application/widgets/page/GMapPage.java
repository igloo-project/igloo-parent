package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.gmap.api.GLatLng;
import fr.openwide.core.wicket.gmap.api.GMapTypeId;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerAnimation;
import fr.openwide.core.wicket.gmap.component.gmap.GMapPanel;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.infoWindow.InfoBubbleBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.infoWindow.InfoBubbleOptions;

public class GMapPage extends WidgetsMainPage {
	private static final long serialVersionUID = -3963117430192776716L;
	
	private GMapPanel gmap;
	
	public GMapPage(PageParameters parameters) {
		super(parameters);
		
		// Création de la carte
		GMapOptions options = new GMapOptions(GMapTypeId.ROADMAP, new GLatLng(-34.397, 150.644), 4);
		gmap = new GMapPanel("gmap", options);
		
		// Ajout d'un marker
		String markerId = "nomLieu";
		GMarkerOptions markerOptions = new GMarkerOptions("nomLieu", new GLatLng(-34.397, 150.645), gmap);
		markerOptions.setAnimation(GMarkerAnimation.DROP);
		
		Label nomLieuLabel = new Label("nomLieu", "Nom d'un lieu");
		add(nomLieuLabel);
		
		// InfoBubble
		InfoBubbleOptions infoOptions = new InfoBubbleOptions(gmap ,markerId, "click", "'1'");
		infoOptions.setPadding(5);
		infoOptions.setBackgroundColor("'#FAF3E6'");
		infoOptions.setBorderRadius(8);
		infoOptions.setArrowSize(10);
		infoOptions.setArrowPosition(20);
		infoOptions.setBorderWidth(1);
		infoOptions.setBorderColor("'#F6DAB9'");
		infoOptions.setDisableAutoPan(false);
		infoOptions.setHideCloseButton(true);
		
		//Les behaviors doivent être ajoutés dans le bon ordre (c.a.d inverse à l'ordre logique)
		nomLieuLabel.add(new InfoBubbleBehavior(infoOptions));
		nomLieuLabel.add(new GMarkerBehavior(markerOptions));
		
		// La carte doit être ajoutée en derniere afin qu'elle apparaisse en premier côté JS. :-/
		add(gmap);
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return WidgetsMainPage.class;
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return GMapPage.class;
	}
}
