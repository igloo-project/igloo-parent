package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.gmap.api.GLatLng;
import fr.openwide.core.wicket.gmap.api.GMapTypeId;

public class GMapOptions implements ChainableStatement, Serializable {
	private static final long serialVersionUID = -1661070056021679177L;

	private Integer zoom;
	
	private GLatLng center;
	
	private GMapTypeId mapTypeId;
	
	@Override
	public String chainLabel() {
		return "gmap";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		if (zoom != null) {
			options.put("zoom", zoom);
		}
		if (center != null) {
			options.put("center", center.getJavaScriptStatement());
		}
		if (mapTypeId != null) {
			options.put("mapTypeId", mapTypeId.getJavaScriptStatement());
		}
		
		CharSequence[] args = new CharSequence[2];
		args[0] = JsUtils.quotes("init");
		args[1] = options.getJavaScriptOptions();
		return args;
	}
	
	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	public GLatLng getCenter() {
		return center;
	}

	public void setCenter(GLatLng center) {
		this.center = center;
	}

	public GMapTypeId getMapTypeId() {
		return mapTypeId;
	}

	public void setMapTypeId(GMapTypeId mapTypeId) {
		this.mapTypeId = mapTypeId;
	}
}
