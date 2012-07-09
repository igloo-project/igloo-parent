package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.staticmap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javatuples.Pair;

import com.google.code.geocoder.model.LatLng;
import com.google.common.collect.Maps;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.GSize;

public class StaticMapUrlBuilder {
	
	private static final int ASCII_CHARACTER_DECIMAL_0 = 48;
	
	private static final int ASCII_CHARACTER_DECIMAL_9 = 57;
	
	private static final int ASCII_CHARACTER_DECIMAL_UPPERCASE_A = 65;
	
	private static final int ASCII_CHARACTER_DECIMAL_UPPERCASE_Z = 90;
	
	private static final int NOMBRE_CHARACTER = 
			ASCII_CHARACTER_DECIMAL_UPPERCASE_Z - ASCII_CHARACTER_DECIMAL_UPPERCASE_A + 1;
	
	private static final int NOMBRE_CHIFFRE = 
			ASCII_CHARACTER_DECIMAL_9 - ASCII_CHARACTER_DECIMAL_0 + 1;
	
	public static final int NOMBRE_MAX_LABEL = NOMBRE_CHARACTER + NOMBRE_CHIFFRE;

	private static final String STATIC_MAP_URL = "http://maps.googleapis.com/maps/api/staticmap?";
	
	private static final String STATIC_MAP_URL_PARAMETER_SEPARATOR = "&";
	
	private static final String STATIC_MAP_URL_PARAMETER_EVAL = "=";
	
	private StaticMapUrlParameter urlParameter = new StaticMapUrlParameter();
	
	private Map<String, Map<Long, Pair<String, String>>> markers = Maps.newHashMap();
	
	private int index = 0;
	
	public StaticMapUrlBuilder(GSize size) {
		setSize(size);
		setSensor(false);
	}
	
	public StaticMapUrlBuilder setSize(GSize size) {
		if (size == null) {
			throw new IllegalArgumentException("La taille de la map doit être définie");
		}
		this.urlParameter.size = size;
		return this;
	}
	
	public StaticMapUrlBuilder setSensor(boolean sensor) {
		this.urlParameter.sensor = sensor;
		return this;
	}
	
	public StaticMapUrlBuilder setScale(int scale) {
		this.urlParameter.scale = scale;
		return this;
	}
	
	public Pair<String, String> addMarker(String color, Long identifier, BigDecimal latitude, BigDecimal longitude) {
		if (markers.containsKey(color) && markers.get(color).containsKey(identifier)) {
			return markers.get(color).get(identifier);
		} else {
			InnerMarker marker = new InnerMarker();
			if (latitude != null && longitude != null) {
				LatLng coord = new LatLng(latitude, longitude);
				marker.localisation = coord.toUrlValue(6);
				marker.color = color;
				marker.label = getLabel();
				index++;
				urlParameter.markers.add(marker);
				if (!markers.containsKey(color)) {
					markers.put(color, Maps.<Long, Pair<String, String>>newHashMap());
				}
				Pair<String, String> result = new Pair<String, String>(marker.color, marker.label);
				markers.get(color).put(identifier, result);
				return result;
			} else {
				return null;
			}
		}
	}
	
	private String getLabel() {
		if (index < NOMBRE_CHARACTER) {
			char label = (char) (index + ASCII_CHARACTER_DECIMAL_UPPERCASE_A);
			return Character.toString(label);
		} else {
			char label = (char) ((index - NOMBRE_CHARACTER + ASCII_CHARACTER_DECIMAL_0));
			return Character.toString(label);
		}
	}
	
	public String build() {
		StringBuilder urlBuilder = new StringBuilder(STATIC_MAP_URL);
		urlBuilder = addParameter(urlBuilder, "sensor", urlParameter.sensor);
		urlBuilder = addParameter(urlBuilder, "size", urlParameter.size.toUrlValue());
		if (urlParameter.scale != null) {
			urlBuilder = addParameter(urlBuilder, "scale", urlParameter.scale);
		}
		
		for(InnerMarker marker : urlParameter.markers) {
			urlBuilder.append(addMarker(marker));
		}
		
		return urlBuilder.toString();
	}
	
	private String addMarker(InnerMarker marker) {
		StringBuilder markerBuilder = new StringBuilder("&markers=");
		markerBuilder.append("label:").append(marker.label);
		markerBuilder.append("%7C");
		markerBuilder.append("color:").append(marker.color);
		markerBuilder.append("%7C");
		markerBuilder.append(marker.localisation);
		
		return markerBuilder.toString();
	}
	
	private StringBuilder addParameter(StringBuilder urlBuilder, String label, Object value) {
		if (value != null) {
			urlBuilder.append(label);
			urlBuilder.append(STATIC_MAP_URL_PARAMETER_EVAL);
			urlBuilder.append(value);
			urlBuilder.append(STATIC_MAP_URL_PARAMETER_SEPARATOR);
		}
		return urlBuilder;
	}
	
	private static final class StaticMapUrlParameter {
		
		private GSize size;
		
		private boolean sensor;
		
		private Integer scale;
		
		private List<InnerMarker> markers = new ArrayList<InnerMarker>();
		
		private StaticMapUrlParameter() {
		}
	}
	
	private static final class InnerMarker {
		
		String localisation;
		
		String color;
		
		String label;
		
		private InnerMarker () {
		}
	}
}