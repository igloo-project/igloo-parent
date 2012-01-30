package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

import com.google.code.geocoder.model.LatLng;

import fr.openwide.core.wicket.gmap.api.utils.GJsStatementUtils;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#CircleOptions"></>
 */
public class GCircleOptions implements ChainableStatement, Serializable {
	private static final long serialVersionUID = 5940551323535642431L;

	private String circleId;
	
	private LatLng center;
	
	private Double radius; // meter
	
	private Boolean clickable;
	
	private Boolean editable;
	
	private String strokeColor;
	
	private Float strokeOpacity; //between 0.0 and 1.0
	
	private Integer strokeWeight; //width in pixels
	
	private Integer zIndex;
	
	public GCircleOptions(String id, LatLng center, Double radius) {
		this.circleId = id;
		this.center = center;
		this.radius = radius;
	}
	
	@Override
	public String chainLabel() {
		return "gmap";
	}

	@Override
	public CharSequence[] statementArgs() {
		if (!isValid()) {
			throw new IllegalArgumentException("A Circle have to be construct with a center, a radius and an id.");
		}
		
		Options options = new Options();
		options.put("center", GJsStatementUtils.getJavaScriptStatement(center));
		options.put("radius", radius);
		
		if (clickable != null) {
			options.put("clickable", clickable);
		}
		if (editable != null) {
			options.put("editable", editable);
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
		if (strokeColor != null) {
			options.put("strokeColor", JsUtils.quotes(strokeColor));
		}
		
		CharSequence[] args = new CharSequence[4];
		args[0] = JsUtils.quotes("createCircle");
		args[1] = JsUtils.quotes(circleId);
		args[2] = options.getJavaScriptOptions();
		return args;
	}

	private Boolean isValid() {
		if (center != null && circleId != null && radius != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getCircleId() {
		return circleId;
	}

	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}

	public LatLng getCenter() {
		return center;
	}

	public void setCenter(LatLng center) {
		this.center = center;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
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
}
