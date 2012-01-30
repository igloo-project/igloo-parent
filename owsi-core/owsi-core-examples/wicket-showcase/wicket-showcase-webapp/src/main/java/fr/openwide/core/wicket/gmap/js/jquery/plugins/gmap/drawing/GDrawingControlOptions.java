package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.drawing;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.gmap.api.drawing.GControlPosition;
import fr.openwide.core.wicket.gmap.api.drawing.GOverlayType;
import fr.openwide.core.wicket.gmap.api.utils.GJsStatementUtils;

/*
 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/reference.html#DrawingControlOptions"></a>
 */
public class GDrawingControlOptions implements Serializable {
	private static final long serialVersionUID = 7966692753547996248L;

	private GControlPosition position;
	private List<GOverlayType> drawingModes;
	private Options options;

	public GDrawingControlOptions() {
		this(GControlPosition.TOP_LEFT, Arrays.asList(GOverlayType.values()));
	}
	
	public GDrawingControlOptions(GControlPosition position) {
		this(position, Arrays.asList(GOverlayType.values()));
	}
	
	public GDrawingControlOptions(GControlPosition position, List<GOverlayType> drawingModes) {
		this.position = position;
		this.drawingModes = drawingModes;
		
		options = new Options();
		options.put("position", GJsStatementUtils.getJavaScriptStatement(this.position));
		options.put("drawingModes", GJsStatementUtils.getJavaScriptStatement(this.drawingModes));
	}
	
	public GControlPosition getPosition() {
		return position;
	}

	public void setPosition(GControlPosition position) {
		this.position = position;
		options.put("position", GJsStatementUtils.getJavaScriptStatement(this.position));
	}

	public List<GOverlayType> getDrawingModes() {
		return drawingModes;
	}

	public void setDrawingModes(List<GOverlayType> drawingModes) {
		this.drawingModes = drawingModes;
		options.put("drawingModes", GJsStatementUtils.getJavaScriptStatement(this.drawingModes));
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}
	
	public CharSequence getJavaScriptOptions(){
		return options.getJavaScriptOptions();
	}
}
