package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap;

import org.odlabs.wiquery.core.options.Options;

import com.google.code.geocoder.model.LatLng;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.GMapTypeId;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.util.GJsStatementUtils;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#MapOptions"></a>
 */
public class GMapOptions extends Options {
	private static final long serialVersionUID = -1661070056021679177L;

	private Integer zoom;
	
	private LatLng center;
	
	private GMapTypeId mapTypeId;
	
	// Control
	private Boolean disableDefaultUI;
	
	private Boolean draggable;
	
	private Boolean panControl;
	
	private Boolean keyboardShortcuts;
	
	private Boolean mapTypeControl;
	
	private Boolean overviewMapControl;
	
	private Boolean rotateControl;
	
	private Boolean scaleControl;
	
	private Boolean scrollwheel;
	
	private Boolean streetViewControl;
	
	private Boolean zoomControl;
	
	private Boolean disableDoubleClickZoom;
	
	/**
	 * Ne fait pas partie de l'API google ; utilisé par le plugin pour gérer l'autofit sur un seul point.
	 */
	private Integer pluginZoomOneMarker;
	
	public GMapOptions(GMapTypeId mapTypeId, LatLng center, Integer zoom) {
		this.mapTypeId = mapTypeId;
		this.center = center;
		this.zoom = zoom;
	}

	@Override
	public CharSequence getJavaScriptOptions() {
		if (!isValid()) {
			throw new IllegalArgumentException("A map must be initialized with mapTypeId, center and zoom.");
		}
		
		if (pluginZoomOneMarker != null) {
			put("pluginZoomOneMarker", pluginZoomOneMarker);
		}
		if (zoom != null) {
			put("zoom", zoom);
			put("pluginDefaultZoom", zoom);
		}
		if (center != null) {
			put("center", GJsStatementUtils.getJavaScriptStatement(center));
			put("pluginDefaultCenter", GJsStatementUtils.getJavaScriptStatement(center));
		}
		if (mapTypeId != null) {
			put("mapTypeId", GJsStatementUtils.getJavaScriptStatement(mapTypeId));
		}
		if (disableDefaultUI != null) {
			put("disableDefaultUI", disableDefaultUI);
		}
		if (draggable != null) {
			put("draggable", draggable);
		}
		if (panControl != null) {
			put("panControl", panControl);
		}
		if (keyboardShortcuts != null) {
			put("keyboardShortcuts", keyboardShortcuts);
		}
		if (mapTypeControl != null) {
			put("mapTypeControl", mapTypeControl);
		}
		if (overviewMapControl != null) {
			put("overviewMapControle", overviewMapControl);
		}
		if (rotateControl != null) {
			put("rotateControl", rotateControl);
		}
		if (scaleControl != null) {
			put("scaleControl", scaleControl);
		}
		if (scrollwheel != null) {
			put("scrollwheel", scrollwheel);
		}
		if (streetViewControl != null) {
			put("streetViewControl", streetViewControl);
		}
		if (zoomControl != null) {
			put("zoomControl", zoomControl);
		}
		if (disableDoubleClickZoom != null) {
			put("disableDoubleClickZoom", disableDoubleClickZoom);
		}
		
		return super.getJavaScriptOptions();
	}
	
	public boolean isValid() {
		return (zoom != null && center != null && mapTypeId != null);
	}
	
	public void setMapEnabled(boolean enabled) {
		this.draggable = enabled;
		this.panControl = enabled;
		this.keyboardShortcuts = enabled;
		this.scrollwheel = enabled;
		this.zoomControl = enabled;
		this.mapTypeControl = enabled;
		this.disableDoubleClickZoom = !enabled;
	}
	
	public void enableInteraction() {
		this.disableDefaultUI = false;
		this.draggable = true;
		this.keyboardShortcuts = true;
		this.scrollwheel = true;
		this.zoomControl = true;
	}
	
	public void disableInteraction() {
		this.disableDefaultUI = true;
		this.draggable = false;
		this.keyboardShortcuts = false;
		this.scrollwheel = false;
		this.zoomControl = false;
	}
	
	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	public LatLng getCenter() {
		return center;
	}

	public void setCenter(LatLng center) {
		this.center = center;
	}

	public GMapTypeId getMapTypeId() {
		return mapTypeId;
	}

	public void setMapTypeId(GMapTypeId mapTypeId) {
		this.mapTypeId = mapTypeId;
	}
	
	public Boolean getDisableDefaultUI() {
		return disableDefaultUI;
	}

	public void setDisableDefaultUI(Boolean disableDefaultUI) {
		this.disableDefaultUI = disableDefaultUI;
	}

	public Boolean getDraggable() {
		return draggable;
	}

	public void setDraggable(Boolean draggable) {
		this.draggable = draggable;
	}

	public Boolean getPanControl() {
		return panControl;
	}

	public void setPanControl(Boolean panControl) {
		this.panControl = panControl;
	}

	public Boolean getKeyboardShortcuts() {
		return keyboardShortcuts;
	}

	public void setKeyboardShortcuts(Boolean keyboardShortcuts) {
		this.keyboardShortcuts = keyboardShortcuts;
	}

	public Boolean getMapTypeControl() {
		return mapTypeControl;
	}

	public void setMapTypeControl(Boolean mapTypeControl) {
		this.mapTypeControl = mapTypeControl;
	}

	public Boolean getOverviewMapControl() {
		return overviewMapControl;
	}

	public void setOverviewMapControl(Boolean overviewMapControl) {
		this.overviewMapControl = overviewMapControl;
	}

	public Boolean getRotateControl() {
		return rotateControl;
	}

	public void setRotateControl(Boolean rotateControl) {
		this.rotateControl = rotateControl;
	}

	public Boolean getScaleControl() {
		return scaleControl;
	}

	public void setScaleControl(Boolean scaleControl) {
		this.scaleControl = scaleControl;
	}

	public Boolean getScrollwheel() {
		return scrollwheel;
	}

	public void setScrollwheel(Boolean scrollwheel) {
		this.scrollwheel = scrollwheel;
	}

	public Boolean getStreetViewControl() {
		return streetViewControl;
	}

	public void setStreetViewControl(Boolean streetViewControl) {
		this.streetViewControl = streetViewControl;
	}

	public Boolean getZoomControl() {
		return zoomControl;
	}

	public void setZoomControl(Boolean zoomControl) {
		this.zoomControl = zoomControl;
	}

	public Boolean getDisableDoubleClickZoom() {
		return disableDoubleClickZoom;
	}

	public void setDisableDoubleClickZoom(Boolean disableDoubleClickZoom) {
		this.disableDoubleClickZoom = disableDoubleClickZoom;
	}
}
