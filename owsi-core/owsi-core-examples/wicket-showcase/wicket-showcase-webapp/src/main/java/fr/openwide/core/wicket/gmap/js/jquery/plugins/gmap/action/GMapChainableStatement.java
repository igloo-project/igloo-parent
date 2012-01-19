package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.action;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

import fr.openwide.core.wicket.gmap.api.GLatLng;
import fr.openwide.core.wicket.gmap.api.GLatLngBounds;
import fr.openwide.core.wicket.gmap.api.gmarker.GMarkerAnimation;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapOptions;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerOptions;

public class GMapChainableStatement {

	private final static String CHAIN_LABEL = "gmap";
	
	/*
	 * Geocoding
	 */
	public static class GeocodingChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;
		
		private String address;
		
		public GeocodingChainableStatement(String address) {
			this.address = address;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (address != null) {
				CharSequence[] statementArgs = new CharSequence[2];
				statementArgs[0] = JsUtils.quotes("geocoding");
				statementArgs[1] = JsUtils.quotes(address);
				return statementArgs;
			} else {
				throw new IllegalArgumentException("GeocodingChainableStatement must be initialized with an address");
			}
		}
	}
	
	/*
	 * Reverse Geocoding
	 */
	public static class ReverseGeocodingChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;
		
		private GLatLng position;
		
		public ReverseGeocodingChainableStatement(GLatLng position) {
			this.position = position;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL; 
		}

		@Override
		public CharSequence[] statementArgs() {
			if (position != null) {
				CharSequence[] statementArgs = new CharSequence[2];
				statementArgs[0] = JsUtils.quotes("reverseGeocoding");
				statementArgs[1] = position.getJavaScriptStatement();
				return statementArgs;
			} else {
				throw new IllegalArgumentException("ReverseGeocodingChainableStatement must be initialized with a position");
			}
		}
	}
	
	/*
	 * Add Marker on Event
	 */
	public static class AddMarkerOnEventChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private GMarkerOptions options;
		
		private String event;
		
		public AddMarkerOnEventChainableStatement(GMarkerOptions options, String event) {
			this.options = options;
			this.event = event;
		}
		
		@Override
		public String chainLabel() {
			return CHAIN_LABEL;
		}

		@Override
		public CharSequence[] statementArgs() {
			if (options == null || event == null) {
				throw new IllegalArgumentException("AddMarkerOnEventChainableStatement must be initialized with an event" +
						"and with GMarkerOptions.");
			} else {
				CharSequence[] statementArgs = options.statementArgs();
				statementArgs[0] = JsUtils.quotes("addMarkerOnMapEvent");
				statementArgs[1] = JsUtils.quotes(event);
				return statementArgs;
			}
		}
	}
	/*
	 * Autofit
	 */
	public static class AutofitChainableStatement implements ChainableStatement, Serializable {
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
	public static class SetOptionsChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private GMapOptions mapOptions;

		public SetOptionsChainableStatement(GMapOptions options) {
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
	public static class PanToChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private GLatLng position;
		
		public PanToChainableStatement(GLatLng position) {
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
				args[1] = position.getJavaScriptStatement();
				return args;
			}
		}
	}
		
	/*
	 * PanTo Marker
	 */
	public static class PanToMarkerChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		public PanToMarkerChainableStatement(String markerId) {
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
	public static class SetZoomChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private Integer zoom;
		
		public SetZoomChainableStatement(Integer zoom) {
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
	public static class FitBoundsChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private GLatLngBounds bounds;
		
		public FitBoundsChainableStatement(GLatLngBounds bounds) {
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
				args[1] = bounds.getJavaScriptStatement();
				return args;
			}
		}
	}
	
	/*
	 * Get Position of a marker from a drag move
	 */
	public static class GetPositionFromDragMoveChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		public GetPositionFromDragMoveChainableStatement(String markerId) {
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
				args[0] = JsUtils.quotes("getPositionFromDragMove");
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
	public static class SetMarkerAnimationChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		private GMarkerAnimation animation;
		
		public SetMarkerAnimationChainableStatement(String markerId, GMarkerAnimation animation) {
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
					args[2] = animation.getJavaScriptStatement();
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
	public static class HideMarkerChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		public HideMarkerChainableStatement(String markerId) {
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
	public static class ShowMarkerChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		public ShowMarkerChainableStatement(String markerId) {
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
	public static class HideAllMarkersExceptChainableStatement implements ChainableStatement, Serializable {
		private static final long serialVersionUID = 8295854739007448244L;

		private String markerId;
		
		public HideAllMarkersExceptChainableStatement(String markerId) {
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
	public static class ShowAllMarkersChainableStatement implements ChainableStatement, Serializable {
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
	public static class ClearMarkersChainableStatement implements ChainableStatement, Serializable {
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
