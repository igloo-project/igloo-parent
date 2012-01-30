package fr.openwide.core.wicket.gmap.api.utils;

import java.util.List;

import org.odlabs.wiquery.core.javascript.JsUtils;

import com.google.code.geocoder.model.LatLng;
import com.google.code.geocoder.model.LatLngBounds;

import fr.openwide.core.wicket.gmap.api.GMapTypeId;
import fr.openwide.core.wicket.gmap.api.GPoint;
import fr.openwide.core.wicket.gmap.api.GSize;
import fr.openwide.core.wicket.gmap.api.directions.GDirectionsWayPoint;
import fr.openwide.core.wicket.gmap.api.directions.GTravelMode;
import fr.openwide.core.wicket.gmap.api.directions.GUnitSystem;
import fr.openwide.core.wicket.gmap.api.drawing.GControlPosition;
import fr.openwide.core.wicket.gmap.api.drawing.GOverlayType;
import fr.openwide.core.wicket.gmap.api.event.GMapEvent;
import fr.openwide.core.wicket.gmap.api.event.GMarkerEvent;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerAnimation;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerImage;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerShape;
import fr.openwide.core.wicket.gmap.js.util.Constructor;

public class GJsStatementUtils {

	/*
	 * Common
	 */
	// LatLng
	public static String getJavaScriptStatement(LatLng latLng) {
		return new Constructor("google.maps.LatLng").add(latLng.getLat()).add(latLng.getLng()).toJS();
	}
	
	// LatLngBounds
	public static String getJavaScriptStatement(LatLngBounds bounds) {
		return new Constructor("google.maps.LatLngBounds").add(getJavaScriptStatement(bounds.getSouthwest()))
			.add(getJavaScriptStatement(bounds.getNortheast())).toJS();
	}
	
	// List<Object>
	@SuppressWarnings("unchecked")
	public static String getJavaScriptStatement(List<?> objects) {
		if (objects.size() > 1) {
			Class<?> clazz = objects.get(0).getClass();
			StringBuilder sb = new StringBuilder("[");
			if(GDirectionsWayPoint.class.equals(clazz)){
				for(Object point : objects) {
					if (sb.length() > 2) {
						sb.append(",");
					}
					sb.append(getJavaScriptStatement((GDirectionsWayPoint) point));
				}
			} else if(GOverlayType.class.equals(clazz)) {
				for(Object type : objects) {
					if (sb.length() > 2) {
						sb.append(",");
					}
					sb.append(getJavaScriptStatement((GOverlayType) type));
				}
			} else if (LatLng.class.equals(clazz)) {
				for(Object latLng : objects) {
					if (sb.length() > 2) {
						sb.append(",");
					}
					sb.append(getJavaScriptStatement((LatLng) latLng));
				}
			} else {
				for(Object list : objects) {
					if (sb.length() > 2) {
						sb.append(",");
					}
					sb.append(getJavaScriptStatement((List<LatLng>) list));
				}
			}
			sb.append("]");
			return sb.toString();
		}
		return "";
	}
	
	// GMapEvent
	public static String getJavaScriptStatement(GMapEvent event) {
		return JsUtils.quotes(event.getValue());
	}
	
	// GMarkerEvent
	public static String getJavaScriptStatement(GMarkerEvent event) {
		return JsUtils.quotes(event.getValue());
	}
	
	/*
	 * Map
	 */
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
	
		if (coords.length > 0) {
			array.append(coords[0]);
			for (int i = 1; i < coords.length; i++) {
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
	
	/*
	 * Directions
	 */
	// GTravelMode
	public static String getJavaScriptStatement(GTravelMode mode) {
		return "google.maps.DirectionsTravelMode." + mode.getValue();
	}
	
	// GUnitSystem
	public static String getJavaScriptStatement(GUnitSystem unit) {
		return "google.maps.DirectionsUnitSystem." + unit.getValue();
	}
	
	// GDirectionsWayPoint
	public static String getJavaScriptStatement(GDirectionsWayPoint point) {
		StringBuilder sb = new StringBuilder("{");
		sb.append("location:");
		sb.append(JsUtils.quotes(point.getLocation()));
		if (point.getStopover() != null) {
			sb.append(",stopover:");
			sb.append(point.getStopover());
		}
		sb.append("}");
		return sb.toString();
	}
	
	/*
	 * Drawing
	 */
	// GOverlayType
	public static String getJavaScriptStatement(GOverlayType type) {
		return "google.maps.drawing.OverlayType." + type.getValue();
	}
	
	//GControlPosition
	public static String getJavaScriptStatement(GControlPosition position) {
		return "google.maps.ControlPosition." + position.getValue();
	}
}
