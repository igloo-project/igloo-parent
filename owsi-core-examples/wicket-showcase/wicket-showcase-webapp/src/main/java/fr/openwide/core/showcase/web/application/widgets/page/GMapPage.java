package fr.openwide.core.showcase.web.application.widgets.page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;

import com.google.code.geocoder.model.LatLng;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.gmap.api.GMapTypeId;
import fr.openwide.core.wicket.gmap.api.directions.GTravelMode;
import fr.openwide.core.wicket.gmap.api.directions.GUnitSystem;
import fr.openwide.core.wicket.gmap.api.drawing.GControlPosition;
import fr.openwide.core.wicket.gmap.api.drawing.GOverlayType;
import fr.openwide.core.wicket.gmap.api.event.GMapEvent;
import fr.openwide.core.wicket.gmap.api.event.GMarkerEvent;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerAnimation;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerImage;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerImageBuilder;
import fr.openwide.core.wicket.gmap.component.map.GMapPanel;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.directions.GDirectionRequest;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing.GDrawingControlOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing.GDrawingManagerBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing.GDrawingManagerOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing.GPolygonOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing.GPolylineOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.infowindow.GInfoBubbleBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.infowindow.GInfoBubbleOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.map.GMapChainableStatement;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.map.GMapOptions;
import fr.openwide.core.wicket.gmap.resource.GMarkerImageResourceReference;

public class GMapPage extends WidgetsMainPage {
	private static final long serialVersionUID = -3963117430192776716L;
	
	private GMapPanel gmap;
	
	public GMapPage(PageParameters parameters) {
		super(parameters);
		setOutputMarkupId(true);
		
		// Création de la carte
		GMapOptions options = new GMapOptions(GMapTypeId.ROADMAP, new LatLng(BigDecimal.valueOf(-34.397), 
				BigDecimal.valueOf(150.644)), 4);
		options.setZoom(15);
		gmap = new GMapPanel("gmap", true, options);
		
		List<Place> places = new ArrayList<Place>();
		Place place1 = new Place("place1", new LatLng("-34.390", "150.670"),
				"gdu-emblem-raid1.svg");
		places.add(place1);
		Place place2 = new Place("place2", new LatLng("-34.395", "150.645"), "gdu-emblem-raid3.svg");
		places.add(place2);
		Place place3 = new Place("place3", new LatLng("-25.395", "111.645"), "gdu-emblem-raid4.svg");
		places.add(place3);
		
		add(new ListView<Place>("list", places) {
			private static final long serialVersionUID = -2594925693594916080L;

			@Override
			protected void populateItem(ListItem<Place> item) {
				final Place place = item.getModelObject();
				
				final String markerId = place.getName();
				GMarkerOptions markerOptions = new GMarkerOptions(place.getName(), place.getPosition(), gmap);
				markerOptions.setAnimation(GMarkerAnimation.DROP);
				markerOptions.setDraggable(false);
				
				GMarkerImageResourceReference reference = new GMarkerImageResourceReference(MainTemplate.class, "images/icons/" + place.getIcon());
				GMarkerImage icon = GMarkerImageBuilder.build().resourceReference(reference).anchor(0, 0).scaledSize(16, 16).create();
				markerOptions.setIcon(icon);
				
				Label placeLabel = new Label("place", place.getName());
				placeLabel.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			
					private static final long serialVersionUID = -4265950097064531551L;
		
					@Override
					public JsScope callback() {
						return JsScope.quickScope(new JsStatement().$(gmap, "")
							.chain(new GMapChainableStatement.HideAllMarkersExcept(place.getName()))
							.chain(new GMapChainableStatement.PanToMarker(markerId))
							.chain(new GMapChainableStatement.SetZoom(10))
							.chain(new GMapChainableStatement.SetMarkerAnimation(place.getName(), GMarkerAnimation.BOUNCE))
						);
					}
				}));
				item.add(placeLabel);
				
				// InfoBubble
				GInfoBubbleOptions infoOptions = new GInfoBubbleOptions(gmap, markerId, GMarkerEvent.CLICK, placeLabel);
				infoOptions.setPadding(5);
				infoOptions.setBackgroundColor("#FAF3E6");
				infoOptions.setBorderRadius(8);
				infoOptions.setArrowSize(10);
				infoOptions.setArrowPosition(20);
				infoOptions.setBorderWidth(1);
				infoOptions.setBorderColor("#F6DAB9");
				infoOptions.setDisableAutoPan(false);
				infoOptions.setHideCloseButton(false);
				
				//Les behaviors doivent être ajoutés dans le bon ordre (c.a.d inverse à l'ordre logique)
				placeLabel.add(new GInfoBubbleBehavior(infoOptions));
				placeLabel.add(new GMarkerBehavior(markerOptions));
				
				Button clearAnimation = new Button("clearAnimation");
				clearAnimation.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
					
							private static final long serialVersionUID = -4265950097064531551L;
				
							@Override
							public JsScope callback() {
								return JsScope.quickScope(new JsStatement().$(gmap, "")
									.chain(new GMapChainableStatement.SetMarkerAnimation(markerId, null)));
							};
				}));
				item.add(clearAnimation);
				
				Button hide = new Button("hide");
				hide.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
					
							private static final long serialVersionUID = -4265950097064531551L;
				
							@Override
							public JsScope callback() {
								return JsScope.quickScope(new JsStatement().$(gmap, "")
									.chain(new GMapChainableStatement.HideMarker(markerId)));
							};
				}));
				item.add(hide);
				
				Button show = new Button("show");
				show.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
					
							private static final long serialVersionUID = -4265950097064531551L;
				
							@Override
							public JsScope callback() {
								return JsScope.quickScope(new JsStatement().$(gmap, "")
									.chain(new GMapChainableStatement.ShowMarker(markerId)));
							};
				}));
				item.add(show);
			}
			
		});
		
		// Clear Markers Button
		Button clearMarkersButton = new Button("clearMarkers");
		clearMarkersButton.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			
					private static final long serialVersionUID = -4265950097064531551L;
		
					@Override
					public JsScope callback() {
						return JsScope.quickScope(new JsStatement().$(gmap, "")
							.chain(new GMapChainableStatement.ClearMarkers()));
					};
		}));
		add(clearMarkersButton);
		
		// Autofit Button
		Button autofitButton = new Button("autofit");
		autofitButton.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			
					private static final long serialVersionUID = -4265950097064531551L;
		
					@Override
					public JsScope callback() {
						return JsScope.quickScope(new JsStatement().$(gmap, "")
								.chain(new GMapChainableStatement.Autofit()));
					};
		}));
		add(autofitButton);
		
		// Show All Markers Button
		Button showAllMarkersButton = new Button("showAll");
		showAllMarkersButton.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			
					private static final long serialVersionUID = -4265950097064531551L;
		
					@Override
					public JsScope callback() {
						return JsScope.quickScope(new JsStatement().$(gmap, "")
								.chain(new GMapChainableStatement.ShowAllMarkers()));
					};
		}));
		add(showAllMarkersButton);
		
		Button clearShapeButton = new Button("clearShape");
		clearShapeButton.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			
					private static final long serialVersionUID = -4265950097064531551L;
		
					@Override
					public JsScope callback() {
						return JsScope.quickScope(new JsStatement().$(gmap, "")
							.chain(new GMapChainableStatement.ClearDrawingShapes()));
					};
		}));
		add(clearShapeButton);
		
		// La carte doit être ajoutée en derniere afin qu'elle apparaisse en premier côté JS. :-/
		add(gmap);
	}
	
	

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		// Marker Create On Map Event
		final GMarkerOptions markerOptions = new GMarkerOptions("createOnEvent", new LatLng(new BigDecimal(-34.397),
				new BigDecimal(150.644)), gmap);
		markerOptions.setAnimation(GMarkerAnimation.BOUNCE);
		markerOptions.setDraggable(true);
		
		GMarkerImageResourceReference reference = new GMarkerImageResourceReference(MainTemplate.class, "images/icons/apport.svg");
		GMarkerImage icon = GMarkerImageBuilder.build().resourceReference(reference).anchor(0, 32).scaledSize(16, 16).create();

		
		markerOptions.setIcon(icon);
		// behavior pour la génération de nouveau marker (ajouter avant le behavior init)
		add(new WiQueryAbstractBehavior() {
			private static final long serialVersionUID = 3947112387454820398L;

			@Override
			public JsStatement statement() {
				return new JsStatement().$(gmap, "")
					.chain(new GMapChainableStatement.AddMarkerOnEvent("createOnEvent", GMapEvent.RIGHTCLICK, 
						"reverseGeocodingOnMarker", gmap.getGeocoderResultAjax().getCallbackUrl().toString(), markerOptions));
			}
		});
		
		
		// Calculate Route Button
		final GDirectionRequest request = new GDirectionRequest("Saint Etienne", "Annecy", GTravelMode.DRIVING, "routeDisplay",
				gmap.getDirectionsResultAjax().getCallbackUrl().toString());
		request.setUnitSystem(GUnitSystem.METRIC);
		
		Button routeButton = new Button("route");
		routeButton.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			
					private static final long serialVersionUID = -4265950097064531551L;
		
					@Override
					public JsScope callback() {
						return JsScope.quickScope(new JsStatement().$(gmap, "").chain(request));
					};
		}));
		add(routeButton);
		
		/*
		 * Drawing Manager
		 */
		// Control Options
		List<GOverlayType> types = new ArrayList<GOverlayType>();
		types.add(GOverlayType.POLYGON);
		types.add(GOverlayType.POLYLINE);
		GDrawingControlOptions drawingControlOptions = new GDrawingControlOptions(GControlPosition.BOTTOM_CENTER, types);
		
		// Polygon Options
		GPolygonOptions polygonOptions = new GPolygonOptions("polygon");
		polygonOptions.setEditable(true);
		
		// Polyline Options
		GPolylineOptions polylineOptions = new GPolylineOptions("polyline");
		polylineOptions.setEditable(true);
		
		GDrawingManagerOptions drawingManagerOptions = new GDrawingManagerOptions(gmap);
		drawingManagerOptions.setDrawingControl(true);
		drawingManagerOptions.setDrawingControlOptions(drawingControlOptions);
		drawingManagerOptions.setPolygonOptions(polygonOptions);
		drawingManagerOptions.setPolylineOptions(polylineOptions);
		
		add(new GDrawingManagerBehavior(drawingManagerOptions));
		
		// Button to get drawing shapes
		Button shapeButton = new Button("shape");
		shapeButton.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
				private static final long serialVersionUID = -4265950097064531551L;
	
				@Override
				public JsScope callback() {
					return JsScope.quickScope(new JsStatement().$(gmap, "")
							.chain(new GMapChainableStatement.GetDrawingShapes(
									gmap.getShapeResultAjax().getCallbackUrl().toString())));
				};
		}));
		add(shapeButton);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return GMapPage.class;
	}
	
	private class Place implements Serializable {
		
		private static final long serialVersionUID = 354186398677112571L;
		
		private String name;
		private LatLng position;
		private String icon;
		
		public Place(String name, LatLng position, String icon){
			this.name = name;
			this.position = position;
			this.icon = icon;
		}

		public String getName() {
			return name;
		}

		public String getIcon() {
			return icon;
		}

		public LatLng getPosition() {
			return position;
		}
	}
}
