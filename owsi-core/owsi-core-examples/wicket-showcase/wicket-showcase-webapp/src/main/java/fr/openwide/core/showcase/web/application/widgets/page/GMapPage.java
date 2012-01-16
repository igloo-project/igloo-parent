package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import wicket.contrib.gmap.GMap2;
import wicket.contrib.gmap.api.GLatLng;

public class GMapPage extends WidgetsMainPage {
	private static final long serialVersionUID = -3963117430192776716L;
	
	private static final String GMAP_API_KEY = "ABQIAAAAYU8GNKhj-QpBRldG7Z0WORQ3unG6DDYLVAh89cGL90iNkBw2LBRBLfDoqsVdv2fwJaRej3mC9vA9CA";
	
	public GMapPage(PageParameters parameters) {
		super(parameters);
		
		GMap2 map = new GMap2("map", GMAP_API_KEY);
		map.setCenter(new GLatLng(52.37649, 4.888573));
		add(map);
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
