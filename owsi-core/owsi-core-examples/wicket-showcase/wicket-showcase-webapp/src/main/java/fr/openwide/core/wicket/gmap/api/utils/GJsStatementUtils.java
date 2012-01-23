package fr.openwide.core.wicket.gmap.api.utils;

import org.odlabs.wiquery.core.javascript.JsUtils;

import com.google.code.geocoder.model.LatLng;
import com.google.code.geocoder.model.LatLngBounds;

import fr.openwide.core.wicket.gmap.api.GMapTypeId;
import fr.openwide.core.wicket.gmap.api.GPoint;
import fr.openwide.core.wicket.gmap.api.GSize;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerAnimation;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerImage;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerShape;
import fr.openwide.core.wicket.gmap.js.util.Constructor;

public class GJsStatementUtils {

	// LatLng
	public static String getJavaScriptStatement(LatLng latLng) {
		return new Constructor("google.maps.LatLng").add(latLng.getLat()).add(latLng.getLng()).toJS();
	}
	
	// LatLngBounds
	public static String getJavaScriptStatement(LatLngBounds bounds) {
		return new Constructor("google.maps.LatLngBounds").add(getJavaScriptStatement(bounds.getSouthwest()))
			.add(getJavaScriptStatement(bounds.getNortheast())).toJS();
	}
	
	// GMarkerAnimation
	public static String getJavaScriptStatement(GMarkerAnimation animation) {
		return "google.maps.Animation." + animation.getValue();
	}
	
	// GMarkerImage
	public static String getJavaScriptStatement(GMarkerImage image) {
		StringBuilder statement = new StringBuilder("new google.maps.MarkerImage(");
		statement.append(JsUtils.quotes(image.getUrl()));
		if (image.getSize() != null) {
			statement.append(",");
			statement.append(getJavaScriptStatement(image.getSize()));
		}
		if (image.getOrigin() != null) {
			statement.append(",");
			statement.append(getJavaScriptStatement(image.getOrigin()));
		}
		if (image.getAnchor() != null) {
			statement.append(",");
			statement.append(getJavaScriptStatement(image.getAnchor()));
		}
		if (image.getScaledSize() != null) {
			statement.append(",");
			statement.append(getJavaScriptStatement(image.getScaledSize()));
		}
		statement.append(")");
		
		return statement.toString();
	}
	
	// GMarkerShape
	public static String getJavaScriptStatement(GMarkerShape shape) {
		return "new google.maps.MarkerShape{type:" + shape.getType().getValue() + ", coords:" + getCoordsJSStatement(shape.getCoords()) + "}";
	}
	
	private static String getCoordsJSStatement(Integer[] coords) {
		StringBuffer array = new StringBuffer();
		array.append("[");
	
		if (coords.length > 0)
		{
			array.append(coords[0]);
	
			for (int i = 1; i < coords.length; i++)
			{
				array.append(", ").append(coords[i]);
			}
		}
		array.append("]");
		return array.toString();
	}
	
	// MapTypeId
	public static String getJavaScriptStatement(GMapTypeId type) {
		return "google.maps.MapTypeId." + type.getValue();
	}
	
	// Point
	public static String getJavaScriptStatement(GPoint point) {
		return new Constructor("google.maps.Point").add(point.getX()).add(point.getY()).toJS();
	}
	
	// Size
	public static String getJavaScriptStatement(GSize size) {
		return new Constructor("google.maps.Size").add(size.getWidth()).add(size.getHeight()).toJS();
	}
}
