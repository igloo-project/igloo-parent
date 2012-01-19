package fr.openwide.core.showcase.web.application.widgets.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.gmap.api.GLatLng;
import fr.openwide.core.wicket.gmap.api.GMapTypeId;
import fr.openwide.core.wicket.gmap.api.Point;
import fr.openwide.core.wicket.gmap.api.Size;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerAnimation;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerImage;
import fr.openwide.core.wicket.gmap.component.gmap.GMapPanel;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.action.GMapChainableStatement;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.infoWindow.GInfoBubbleBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.infoWindow.GInfoBubbleOptions;

public class GMapPage extends WidgetsMainPage {
	private static final long serialVersionUID = -3963117430192776716L;
	
	private GMapPanel gmap;
	
	public GMapPage(PageParameters parameters) {
		super(parameters);
		
		// Création de la carte
		GMapOptions options = new GMapOptions(GMapTypeId.ROADMAP, new GLatLng(-34.397, 150.644), 4);
		options.setZoom(15);
		gmap = new GMapPanel("gmap", options);
		
		List<Place> places = new ArrayList<Place>();
		Place place1 = new Place("place1", new GLatLng(-34.390, 150.670), "images/icons/gdu-emblem-raid1.svg");
		places.add(place1);
		Place place2 = new Place("place2", new GLatLng(-34.395, 150.645), "images/icons/gdu-emblem-raid3.svg");
		places.add(place2);
		Place place3 = new Place("place3", new GLatLng(-25.395, 111.645), "images/icons/gdu-emblem-raid4.svg");
		places.add(place3);
		
		add(new ListView<Place>("list", places) {
			private static final long serialVersionUID = -2594925693594916080L;

			@Override
			protected void populateItem(ListItem<Place> item) {
				final Place place = item.getModelObject();
				
				final String markerId = place.getName();
				GMarkerOptions markerOptions = new GMarkerOptions(place.getName(), place.getPosition(), gmap);
				markerOptions.setAnimation(GMarkerAnimation.DROP);
				markerOptions.setDraggable(true);
				
				ResourceReference resourceReference = new SharedResourceReference(MainTemplate.class, place.getIcon());
				GMarkerImage icon = new GMarkerImage(resourceReference, new Point(0, 32), new Point(0,0), new Size(16, 16),
						new Size(16, 16));
				markerOptions.setIcon(icon);

				Label placeLabel = new Label("place", place.getName());
				placeLabel.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			
					private static final long serialVersionUID = -4265950097064531551L;
		
					@Override
					public JsScope callback() {
						return JsScope.quickScope(new JsStatement().$(gmap, "")
//							.chain(new GMapChainableStatement.HideAllMarkersExceptChainableStatement(place.getName()))
							.chain(new GMapChainableStatement.PanToMarkerChainableStatement(markerId))
							.chain(new GMapChainableStatement.SetZoomChainableStatement(10))
							.chain(new GMapChainableStatement.SetMarkerAnimationChainableStatement(place.getName(), GMarkerAnimation.BOUNCE))
						);
					}
				}));
				item.add(placeLabel);
				
				// InfoBubble
				GInfoBubbleOptions infoOptions = new GInfoBubbleOptions(gmap, markerId, "click", "1");
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
				placeLabel.add(new WiQueryAbstractBehavior() {
					private static final long serialVersionUID = -5555894438701693603L;

					@Override
					public JsStatement statement() {
						return new JsStatement().$(gmap, "")
							.chain(new GMapChainableStatement.GetPositionFromDragMoveChainableStatement(markerId));
					}
				});
				placeLabel.add(new GMarkerBehavior(markerOptions));
				
				Button clearAnimation = new Button("clearAnimation");
				clearAnimation.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
					
							private static final long serialVersionUID = -4265950097064531551L;
				
							@Override
							public JsScope callback() {
								return JsScope.quickScope(new JsStatement().$(gmap, "")
									.chain(new GMapChainableStatement.SetMarkerAnimationChainableStatement(markerId, null)));
							};
				}));
				item.add(clearAnimation);
				
				Button hide = new Button("hide");
				hide.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
					
							private static final long serialVersionUID = -4265950097064531551L;
				
							@Override
							public JsScope callback() {
								return JsScope.quickScope(new JsStatement().$(gmap, "")
									.chain(new GMapChainableStatement.HideMarkerChainableStatement(markerId)));
							};
				}));
				item.add(hide);
				
				Button show = new Button("show");
				show.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
					
							private static final long serialVersionUID = -4265950097064531551L;
				
							@Override
							public JsScope callback() {
								return JsScope.quickScope(new JsStatement().$(gmap, "")
									.chain(new GMapChainableStatement.ShowMarkerChainableStatement(markerId)));
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
							.chain(new GMapChainableStatement.ClearMarkersChainableStatement()));
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
								.chain(new GMapChainableStatement.AutofitChainableStatement()));
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
								.chain(new GMapChainableStatement.ShowAllMarkersChainableStatement()));
					};
		}));
		add(showAllMarkersButton);
		
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
	
	private class Place {
		private String name;
		private GLatLng position;
		private String icon;
		
		public Place(String name, GLatLng position, String icon){
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

		public GLatLng getPosition() {
			return position;
		}
	}
}
