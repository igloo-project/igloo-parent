package fr.openwide.core.wicket.gmap.api.directions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.code.geocoder.model.LatLng;

public class GPolyline implements Serializable {
	private static final long serialVersionUID = 3150102457554516954L;

	private String polyline;
	private List<LatLng> points;
	
	public GPolyline(){
	}
	
	/*
	 * Decoder Methods extracts from see <a href="https://github.com/scoutant/polyline-decoder"></>
	 */
	public static List<LatLng> decodePolyline(String polyline) {
		List<LatLng> points = new ArrayList<LatLng>();
		int index = 0;
		int lat = 0, lng = 0;
		
		while (index < polyline.length()) {
			int b, shift = 0, result = 0;
			do {
				b = polyline.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = polyline.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng(BigDecimal.valueOf((double)lat/1E5), BigDecimal.valueOf((double)lng/1E5));
			points.add(p);
		}
		return points;
	}

	public String getPolyline() {
		return polyline;
	}

	public void setPolyline(String polyline) {
		this.polyline = polyline;
	}

	public List<LatLng> getPoints() {
		return points;
	}

	public void setPoints(List<LatLng> points) {
		this.points = points;
	}
}
