package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.map;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

import com.google.code.geocoder.model.LatLng;

import fr.openwide.core.wicket.gmap.api.GMapTypeId;
import fr.openwide.core.wicket.gmap.api.utils.GJsStatementUtils;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#MapOptions"></a>
 */

public class GMapOptions implements ChainableStatement, Serializable {
	private static final long serialVersionUID = -1661070056021679177L;

	private Integer zoom;
	
	private LatLng center;
	
	private GMapTypeId mapTypeId;
	
	// Control
	private Boolean disableDefaultUI;

	private Boolean draggable;
	
	private Boolean keyboardShortcuts;
	
	private Boolean mapTypeControl;
	
	private Boolean overviewMapControl;
	
	private Boolean rotateControl;
	
	private Boolean scaleControl;
	
	private Boolean scrollwheel;
	
	private Boolean streetViewControl;
	
	private Boolean zoomControl;
	
	public GMapOptions(GMapTypeId mapTypeId, LatLng center, Integer zoom) {
		this.mapTypeId = mapTypeId;
		this.center = center;
		this.zoom = zoom;
	}

	@Override
	public String chainLabel() {
		return "gmap";
	}

	@Override
	public CharSequence[] statementArgs() {
		if (!isValid()) {
			throw new IllegalArgumentException("A map must be initialized with mapTypeId, center and zoom.");
		}
		
		Options options = new Options();
		if (zoom != null) {
			options.put("zoom", zoom);
		}
		if (center != null) {
			options.put("center", GJsStatementUtils.getJavaScriptStatement(center));
		}
		if (mapTypeId != null) {
			options.put("mapTypeId", GJsStatementUtils.getJavaScriptStatement(mapTypeId));
		}
		if (disableDefaultUI != null) {
			options.put("disableDefaultUI", disableDefaultUI);
		}
		if (draggable != null) {
			options.put("draggable", draggable);
		}
		if (keyboardShortcuts != null) {
			options.put("keyboardShortcuts", keyboardShortcuts);
		}
		if (mapTypeControl != null) {
			options.put("mapTypeControl", mapTypeControl);
		}
		if (overviewMapControl != null) {
			options.put("overviewMapControle", overviewMapControl);
		}
		if (rotateControl != null) {
			options.put("rotateControl", rotateControl);
		}
		if (scaleControl != null) {
			options.put("scaleControl", scaleControl);
		}
		if (scrollwheel != null) {
			options.put("scrollwheel", scrollwheel);
		}
		if (streetViewControl != null) {
			options.put("streetViewControl", streetViewControl);
		}
		if (zoomControl != null) {
			options.put("zoomControl", zoomControl);
		}
		
		CharSequence[] args = new CharSequence[2];
		args[0] = JsUtils.quotes("init");
		args[1] = options.getJavaScriptOptions();
		return args;
	}
	
	public boolean isValid() {
		if (zoom != null && center != null && mapTypeId != null) {
			return true;
		} else {
			return false;
		}
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
}
