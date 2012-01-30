package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.gmap.api.drawing.GOverlayType;
import fr.openwide.core.wicket.gmap.api.utils.GJsStatementUtils;
import fr.openwide.core.wicket.gmap.component.map.GMapPanel;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.gmarker.GMarkerOptions;

public class GDrawingManagerOptions implements ChainableStatement, Serializable {
	private static final long serialVersionUID = 2837708165479106774L;
	
	private GMapPanel map;
	
	private Boolean drawingControl;
	
	private GOverlayType drawingMode;
	
	private GDrawingControlOptions drawingControlOptions;
	
	private GMarkerOptions markerOptions;
	
	private GPolygonOptions polygonOptions;
	
	private GCircleOptions circleOptions;
	
	private GPolylineOptions polylineOptions;
	
	private GRectangleOptions rectangleOptions;
	
	public GDrawingManagerOptions(GMapPanel map) {
		this.map = map;
	}
	
	@Override
	public String chainLabel() {
		return "gmap";
	}
	
	@Override
	public CharSequence[] statementArgs() {
		if (!isValid()) {
			throw new IllegalArgumentException("Drawing Manager has to be initialized with a map");
		}
		Options options = new Options();
		if (drawingControl != null) {
			options.put("drawingControl", drawingControl);
		}
		if (drawingMode != null) {
			options.put("drawingMode", GJsStatementUtils.getJavaScriptStatement(drawingMode));
		}
		if (drawingControlOptions != null) {
			options.put("drawingControlOptions", drawingControlOptions.getJavaScriptOptions().toString());
		}
		if (markerOptions != null) {
			CharSequence[] statementArgs = markerOptions.statementArgs();
			options.put("markerOptions", statementArgs[2].toString());
		}
		if (polygonOptions != null) {
			CharSequence[] statementArgs = polygonOptions.statementArgs();
			options.put("polygonOptions", statementArgs[2].toString());
		}
		if (circleOptions != null) {
			CharSequence[] statementArgs = circleOptions.statementArgs();
			options.put("circleOptions", statementArgs[2].toString());
		}
		if (polylineOptions != null) {
			CharSequence[] statementArgs = polylineOptions.statementArgs();
			options.put("polylineOptions", statementArgs[2].toString());
		}
		if (rectangleOptions != null) {
			CharSequence[] statementArgs = rectangleOptions.statementArgs();
			options.put("rectangleOptions", statementArgs[2].toString());
		}
		
		CharSequence[] args = new CharSequence[2];
		args[0] = JsUtils.quotes("createDrawingManager");
		args[1] = options.getJavaScriptOptions();
		return args;
	}

	private Boolean isValid() {
		if (map != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getDrawingControl() {
		return drawingControl;
	}

	public void setDrawingControl(Boolean drawingControl) {
		this.drawingControl = drawingControl;
	}

	public GOverlayType getDrawingMode() {
		return drawingMode;
	}

	public void setDrawingMode(GOverlayType drawingMode) {
		this.drawingMode = drawingMode;
	}

	public GDrawingControlOptions getDrawingControlOptions() {
		return drawingControlOptions;
	}

	public void setDrawingControlOptions(GDrawingControlOptions drawingControlOptions) {
		this.drawingControlOptions = drawingControlOptions;
	}

	public GMarkerOptions getMarkerOptions() {
		return markerOptions;
	}

	public void setMarkerOptions(GMarkerOptions markerOptions) {
		this.markerOptions = markerOptions;
	}

	public GPolygonOptions getPolygonOptions() {
		return polygonOptions;
	}

	public void setPolygonOptions(GPolygonOptions polygonOptions) {
		this.polygonOptions = polygonOptions;
	}

	public GCircleOptions getCircleOptions() {
		return circleOptions;
	}

	public void setCircleOptions(GCircleOptions circleOptions) {
		this.circleOptions = circleOptions;
	}

	public GPolylineOptions getPolylineOptions() {
		return polylineOptions;
	}

	public void setPolylineOptions(GPolylineOptions polylineOptions) {
		this.polylineOptions = polylineOptions;
	}

	public GRectangleOptions getRectangleOptions() {
		return rectangleOptions;
	}

	public void setRectangleOptions(GRectangleOptions rectangleOptions) {
		this.rectangleOptions = rectangleOptions;
	}

	public GMapPanel getMap() {
		return map;
	}

	public void setMap(GMapPanel map) {
		this.map = map;
	}
}
