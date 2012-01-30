package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

import com.google.code.geocoder.model.LatLngBounds;

import fr.openwide.core.wicket.gmap.api.utils.GJsStatementUtils;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#RectangleOptions"></a>
 */
/*
 * ! Method createRectangle doest not exist in JS !
 */
public class GRectangleOptions implements ChainableStatement, Serializable {
	private static final long serialVersionUID = -4087557169988132351L;

	private String rectangleId;
	
	private LatLngBounds bounds;
	
	private Boolean clickable;
	
	private Boolean editable;
	
	private String strokeColor;
	
	private Float strokeOpacity; //between 0.0 and 1.0
	
	private Integer strokeWeight; //width in pixels
	
	private Integer zIndex;
	
	public GRectangleOptions(String id, LatLngBounds bounds) {
		this.rectangleId = id;
		this.bounds = bounds;
	}
	
	@Override
	public String chainLabel() {
		return "gmap";
	}

	@Override
	public CharSequence[] statementArgs() {
		if (!isValid()) {
		throw new IllegalArgumentException("A Rectangle have to be construct with a bound and an id.");
		}
	
		Options options = new Options();
		options.put("bounds", GJsStatementUtils.getJavaScriptStatement(bounds));
		
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
		args[0] = JsUtils.quotes("createRectangle");
		args[1] = JsUtils.quotes(rectangleId);
		args[2] = options.getJavaScriptOptions();
		return args;
	}
	
	private Boolean isValid() {
		if (bounds != null && rectangleId != null) {
			return true;
		} else {
			return false;
		}
	}

	public String getRectangleId() {
		return rectangleId;
	}

	public void setRectangleId(String rectangleId) {
		this.rectangleId = rectangleId;
	}

	public LatLngBounds getBounds() {
		return bounds;
	}

	public void setBounds(LatLngBounds bounds) {
		this.bounds = bounds;
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
