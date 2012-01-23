package fr.openwide.core.wicket.gmap.component.gmap;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.wicket.gmap.api.GPoint;
import fr.openwide.core.wicket.gmap.api.GSize;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerAnimation;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerImage;
import fr.openwide.core.wicket.gmap.component.GMapHeaderContributor;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.map.GMapBehavior;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.map.GMapChainableStatement;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.map.GMapOptions;

public class GMapPanel extends Panel {
	private static final long serialVersionUID = -904534558476084988L;
	
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	static {
		OBJECT_MAPPER.setPropertyNamingStrategy(new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy());
		OBJECT_MAPPER.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false); 
	}
	
	private AbstractDefaultAjaxBehavior updateAjax;
	
	private GMapOptions options;
	
	private GeocoderResult geocoderResult = new GeocoderResult();
	
	public GMapPanel(String id, GMapOptions options) {
		this(id, null, null, options);
	}
	
	public GMapPanel(String id, Locale region, GMapOptions options) {
		this(id, region, null, null);
	}
	
	public GMapPanel(String id, Locale region, Locale locale, GMapOptions options) {
		super(id);
		
		this.options = options;
		
		add(new GMapHeaderContributor(region, locale));
		
		updateAjax = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = -7684282452805422195L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				Request request = RequestCycle.get().getRequest();
				String result = request.getPostParameters().getParameterValue("result").toString();
				try {
					geocoderResult = OBJECT_MAPPER.readValue(result, GeocoderResult.class);
				} catch (IOException e) {
					new RuntimeException(e);
				}
			}
		};
		add(updateAjax);
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		// Marker Create On Map Event
		final GMarkerOptions markerOptions = new GMarkerOptions("createOnEvent", new LatLng(new BigDecimal(-34.397),
				new BigDecimal(150.644)), this);
		markerOptions.setAnimation(GMarkerAnimation.BOUNCE);
		markerOptions.setDraggable(true);
		
		ResourceReference resourceReference = new SharedResourceReference(MainTemplate.class, "images/icons/apport.svg");
		GMarkerImage icon = new GMarkerImage(resourceReference, new GPoint(0, 32), new GPoint(0,0), new GSize(16, 16),
				new GSize(16, 16));
		markerOptions.setIcon(icon);
		// behavior pour la génération de nouveau marker (ajouter avant le behavior init)
		add(new WiQueryAbstractBehavior() {
			private static final long serialVersionUID = 3947112387454820398L;

			@Override
			public JsStatement statement() {
				return new JsStatement().$(GMapPanel.this, "")
					.chain(new GMapChainableStatement.AddMarkerOnEvent("createOnEvent", "rightclick", "reverseGeocodingOnMarker",
						updateAjax.getCallbackUrl().toString(), markerOptions));
			}
		});
		
		// Behavior d'initialisation de la carte
		add(new GMapBehavior(options));
	}
	
	public GeocoderResult getGeocoderResult() {
		return geocoderResult;
	}
}