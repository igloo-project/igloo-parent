package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing;

import java.io.Serializable;
import java.util.List;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

import com.google.code.geocoder.model.LatLng;

import fr.openwide.core.wicket.gmap.api.utils.GJsStatementUtils;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#PolygonOptions"></a>
 */
public class GPolygonOptions implements ChainableStatement, Serializable {
	private static final long serialVersionUID = 4356073655152717575L;
	
	private Options options;
	
	private String polygonId;
	
	private Boolean clickable;
	
	private Boolean editable;
	
	private String fillColor;
	
	private String fillOpacity;
	
	private Boolean geodesic;
	
	private String strokeColor;
	
	private Float strokeOpacity; //between 0.0 and 1.0
	
	private Integer strokeWeight; //width in pixels
	
	private Integer zIndex;
	
	List<List<LatLng>> paths;
	
	public GPolygonOptions(String id) {
		this.polygonId = id;
	}
	
	public GPolygonOptions(String id, List<List<LatLng>> paths){
		this.paths = paths;
		this.polygonId = id;
	}

	@Override
	public String chainLabel() {
		return "gmap";
	}

	@Override
	public CharSequence[] statementArgs() {
		if (!isValid()) {
			throw new IllegalArgumentException("Polycon has to be set with an id.");
		}
		options = new Options();
		if (paths != null) {
			options.put("paths", GJsStatementUtils.getJavaScriptStatement(paths));
		}
		if (clickable != null) {
			options.put("clickable", clickable);
		}
		if (editable != null) {
			options.put("editable", editable);
		}
		if (geodesic != null) {
			options.put("geodesic", geodesic);
		}
		if (zIndex != null) {
			options.put("zIndex", zIndex);
		}
		if (strokeWeight != null) {
			options.put("strokeWeight", strokeWeight);
		}
		if (strokeOpacity != null) {
			options.put("strokeOpacity", strokeOpacity);
		}
		if (fillOpacity != null) {
			options.put("fillOpacity", JsUtils.quotes(fillOpacity));
		}
		if (fillColor != null) {
			options.put("fillColor", JsUtils.quotes(fillColor));
		}
		if (strokeColor != null) {
			options.put("strokeColor", JsUtils.quotes(strokeColor));
		}
		
		CharSequence[] args = new CharSequence[4];
		args[0] = JsUtils.quotes("createPolygon");
		args[1] = JsUtils.quotes(polygonId);
		args[2] = options.getJavaScriptOptions();
		return args;
	}
	
	private Boolean isValid() {
		if (polygonId != null) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getClickable() {
		return clickable;
	}

	public void setClickable(Boolean clickable) {
		this.clickable = clickable;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public String getFillOpacity() {
		return fillOpacity;
	}

	public void setFillOpacity(String fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	public Boolean getGeodesic() {
		return geodesic;
	}

	public void setGeodesic(Boolean geodesic) {
		this.geodesic = geodesic;
	}

	public String getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	public Float getStrokeOpacity() {
		return strokeOpacity;
	}

	public void setStrokeOpacity(Float strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
	}

	public Integer getStrokeWeight() {
		return strokeWeight;
	}

	public void setStrokeWeight(Integer strokeWeight) {
		this.strokeWeight = strokeWeight;
	}

	public Integer getzIndex() {
		return zIndex;
	}

	public void setzIndex(Integer zIndex) {
		this.zIndex = zIndex;
	}

	public List<List<LatLng>> getPaths() {
		return paths;
	}

	public void setPaths(List<List<LatLng>> paths) {
		this.paths = paths;
	}
}
