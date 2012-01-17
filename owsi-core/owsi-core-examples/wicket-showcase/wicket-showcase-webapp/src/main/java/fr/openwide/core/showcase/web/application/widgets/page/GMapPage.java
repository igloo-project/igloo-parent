package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.gmap.api.GLatLng;
import fr.openwide.core.wicket.gmap.api.GMapTypeId;
import fr.openwide.core.wicket.gmap.component.GMapPanel;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapOptions;

public class GMapPage extends WidgetsMainPage {
	private static final long serialVersionUID = -3963117430192776716L;
	
	public GMapPage(PageParameters parameters) {
		super(parameters);
		
		GMapOptions options = new GMapOptions(GMapTypeId.ROADMAP, new GLatLng(-34.397, 150.644), 4);
		
		GMapPanel gmap = new GMapPanel("gmap", options);
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
