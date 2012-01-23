package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.map;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

import com.google.code.geocoder.model.LatLng;
import com.google.code.geocoder.model.LatLngBounds;

import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerAnimation;
import fr.openwide.core.wicket.gmap.api.utils.GJsStatementUtils;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerOptions;

public class GMapChainableStatement {

	private final static String CHAIN_LABEL = "gmap";
	
	/*
	 * Geocoding
	 */
	public static class Geocoding implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;
		
		private String address;
		private String callback;
		
		public Geocoding(String address, String callback) {
			this.address = address;
			this.callback = callback;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (address != null && callback != null) {
				CharSequence[] statementArgs = new CharSequence[3];
				statementArgs[0] = JsUtils.quotes("geocoding");
				statementArgs[1] = JsUtils.quotes(address);
				statementArgs[2] = JsUtils.quotes(callback);
				return statementArgs;
			} else {
				throw new IllegalArgumentException("Geocoding must be initialized with an address and a callback function");
			}
		}
	}
	
	/*
	 * Reverse Geocoding
	 */
	public static class ReverseGeocoding implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;
		
		private LatLng position;
		private String callback;
		private String markerId;
		private String callbackUrl;
		
		public ReverseGeocoding(LatLng position, String markerId, String callback, String callbackUrl) {
			this.position = position;
			this.callback = callback;
			this.markerId = markerId;
			this.callbackUrl = callbackUrl;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL; 
		}

		@Override
		public CharSequence[] statementArgs() {
			if (position != null && callback != null) {
				CharSequence[] statementArgs = new CharSequence[2];
				statementArgs[0] = JsUtils.quotes("reverseGeocoding");
				statementArgs[1] = GJsStatementUtils.getJavaScriptStatement(position);
				statementArgs[2] = (markerId.isEmpty() ? JsUtils.quotes("") : JsUtils.quotes(markerId));
				statementArgs[3] = (callbackUrl.isEmpty() ? JsUtils.quotes("") : JsUtils.quotes(callbackUrl));
				return statementArgs;
			} else {
				throw new IllegalArgumentException("ReverseGeocodingChainableStatement must be initialized with a position" +
						"and a callback function");
			}
		}
	}
	
	/*
	 * Add Marker on Event
	 */
	public static class AddMarkerOnEvent implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private GMarkerOptions options;
		private String callback;
		private String callbackUrl;
		private String event;
		private String markerId;
		
		public AddMarkerOnEvent(String markerId, String event, String callback, String callbackUrl, GMarkerOptions options) {
			this.markerId = markerId;
			this.options = options;
			this.event = event;
			this.callbackUrl = callbackUrl;
			this.callback = callback;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (options == null || event == null || callbackUrl == null) {
				throw new IllegalArgumentException("AddMarkerOnEventChainableStatement must be initialized with an event," +
						"a callback URL and with GMarkerOptions.");
			} else {
				CharSequence[] oldArgs = options.statementArgs();
				CharSequence[] args = new CharSequence[6];
				args[0] = JsUtils.quotes("addMarkerOnMapEvent");
				args[1] = JsUtils.quotes(markerId);
				args[2] = JsUtils.quotes(event);
				args[3] = JsUtils.quotes(callback);
				args[4] = JsUtils.quotes(callbackUrl);
				args[5] = oldArgs[2];
				return args;
			}
		}
	}
	/*
	 * Autofit
	 */
	public static class Autofit implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL; 
		}

		@Override
		public CharSequence[] statementArgs() {
			CharSequence[] statementArgs = new CharSequence[1];
			statementArgs[0] = JsUtils.quotes("autofit");
			return statementArgs;
		}
	}
	/*
	 * Set Options of a map
	 */
	public static class SetOptions implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private GMapOptions mapOptions;

		public SetOptions(GMapOptions options) {
			this.mapOptions = options;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL; 
		}

		@Override
		public CharSequence[] statementArgs() {
			CharSequence[] statementArgs = mapOptions.statementArgs();
			statementArgs[0] = JsUtils.quotes("setOptions");
			return statementArgs;
		}
	}
	
	/*
	 * PanTo
	 */
	public static class PanTo implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private LatLng position;
		
		public PanTo(LatLng position) {
			this.position = position;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (position == null) {
				throw new IllegalArgumentException("PanTo must be initialized with a position.");
			} else {
				CharSequence[] args = new CharSequence[2];
				args[0] = JsUtils.quotes("panTo");
				args[1] = GJsStatementUtils.getJavaScriptStatement(position);
				return args;
			}
		}
	}
		
	/*
	 * PanTo Marker
	 */
	public static class PanToMarker implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		public PanToMarker(String markerId) {
			this.markerId = markerId;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (markerId == null) {
				throw new IllegalArgumentException("PanToMarker must be initialized with a markerId.");
			} else {
				CharSequence[] args = new CharSequence[2];
				args[0] = JsUtils.quotes("panToMarker");
				args[1] = JsUtils.quotes(markerId);
				return args;
			}
		}
	}
	
	/*
	 * Set Zoom
	 */
	public static class SetZoom implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private Integer zoom;
		
		public SetZoom(Integer zoom) {
			this.zoom = zoom;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (zoom == null) {
				throw new IllegalArgumentException("setZoom must be initialized with a position.");
			} else {
				CharSequence[] args = new CharSequence[2];
				args[0] = JsUtils.quotes("setZoom");
				args[1] = zoom.toString();
				return args;
			}
		}
	}
	
	/*
	 * Fit Bounds
	 */
	public static class FitBounds implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private LatLngBounds bounds;
		
		public FitBounds(LatLngBounds bounds) {
			this.bounds = bounds;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (bounds == null) {
				throw new IllegalArgumentException("FitBounds must be initialized with bounds.");
			} else {
				CharSequence[] args = new CharSequence[2];
				args[0] = JsUtils.quotes("fitBounds");
				args[1] = GJsStatementUtils.getJavaScriptStatement(bounds);
				return args;
			}
		}
	}
	
	/*
	 * Get Position of a marker from a drag move
	 */
	public static class updatePositionFromDragMove implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		public updatePositionFromDragMove(String markerId) {
			this.markerId = markerId;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (markerId != null) {
				CharSequence[] args = new CharSequence[2];
				args[0] = JsUtils.quotes("updatePositionFromDragMove");
				args[1] = JsUtils.quotes(markerId);
				return args;
			} else {
				throw new IllegalArgumentException("SetMarkerAnimation must be initialized with markerId and animation");
			}
		}
	}
	
	
	/*
	 * Set Marker Animation
	 */
	public static class SetMarkerAnimation implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		private GMarkerAnimation animation;
		
		public SetMarkerAnimation(String markerId, GMarkerAnimation animation) {
			this.animation = animation;
			this.markerId = markerId;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (markerId != null) {
				CharSequence[] args = new CharSequence[3];
				args[0] = JsUtils.quotes("setMarkerAnimation");
				args[1] = JsUtils.quotes(markerId);
				if (animation == null){
					args[2] = null;
				} else {
					args[2] = GJsStatementUtils.getJavaScriptStatement(animation);
				}
				return args;
			} else {
				throw new IllegalArgumentException("SetMarkerAnimation must be initialized with markerId.");
				
			}
		}
	}
	
	/*
	 * Hide Marker
	 */
	public static class HideMarker implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		public HideMarker(String markerId) {
			this.markerId = markerId;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (markerId != null) {
				CharSequence[] args = new CharSequence[2];
				args[0] = JsUtils.quotes("hideMarker");
				args[1] = JsUtils.quotes(markerId);
				return args;
			} else {
				throw new IllegalArgumentException("HideMarker must be initialized with markerId.");
				
			}
		}
	}
	
	/*
	 * Show Marker
	 */
	public static class ShowMarker implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		public ShowMarker(String markerId) {
			this.markerId = markerId;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (markerId != null) {
				CharSequence[] args = new CharSequence[2];
				args[0] = JsUtils.quotes("showMarker");
				args[1] = JsUtils.quotes(markerId);
				return args;
			} else {
				throw new IllegalArgumentException("ShowMarker must be initialized with markerId.");
				
			}
		}
	}
	
	/*
	 * Hide Markers
	 */
	public static class HideAllMarkersExcept implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		public HideAllMarkersExcept(String markerId) {
			this.markerId = markerId;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (markerId != null) {
				CharSequence[] args = new CharSequence[2];
				args[0] = JsUtils.quotes("hideAllMarkersExcept");
				args[1] = JsUtils.quotes(markerId);
				return args;
			} else {
				throw new IllegalArgumentException("FitBounds must be initialized with bounds.");
				
			}
		}
	}
	
	/*
	 * Show All Markers
	 */
	public static class ShowAllMarkers implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			CharSequence[] args = new CharSequence[1];
			args[0] = JsUtils.quotes("showAllMarkers");
			return args;
		}
	}
	
	/*
	 * Clear All Markers
	 */
	public static class ClearMarkers implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			CharSequence[] args = new CharSequence[1];
			args[0] = JsUtils.quotes("clearMarkers");
			return args;
		}
	}
}
