package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap;

import java.util.List;

import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.drawing.DrawingControlOptions;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.drawing.GControlPosition;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.drawing.GOverlayType;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.drawing.PolygonOptions;

/*
 * see <a href="https://developers.google.com/maps/documentation/javascript/reference#DrawingManagerOptions"></a>
 */
public class DrawingManagerOptions extends Options {

	private static final long serialVersionUID = -132850114430245428L;
	
	private Boolean drawingControl;
	
	private DrawingControlOptions drawingControlOptions;
	
	private PolygonOptions polygonOptions;
	
	public DrawingManagerOptions(Boolean drawingControl) {
		this.drawingControl = drawingControl;
	}
	
	public DrawingManagerOptions(Boolean drawingControl, GControlPosition position, List<GOverlayType> drawingModes) {
		this.drawingControl = drawingControl;
		this.drawingControlOptions = new DrawingControlOptions(position, drawingModes);
	}
	
	@Override
	public CharSequence getJavaScriptOptions() {
		if (drawingControl != null) {
			put("drawingControl", drawingControl);
		}
		if (drawingControlOptions != null) {
			put("drawingControlOptions", drawingControlOptions.getJavaScriptOptions().toString());
		}
		if (polygonOptions != null) {
			put("polygonOptions", polygonOptions.getJavaScriptOptions().toString());
		}
		
		return super.getJavaScriptOptions();
	}
	
	public Boolean getDrawingControl() {
		return drawingControl;
	}
	
	public void setDrawingControl(Boolean drawingControl) {
		this.drawingControl = drawingControl;
	}
	
	public DrawingControlOptions getDrawingControlOptions() {
		return drawingControlOptions;
	}
	
	public void setDrawingControlOptions(DrawingControlOptions drawingControlOptions) {
		this.drawingControlOptions = drawingControlOptions;
	}
	
	public PolygonOptions getPolygonOptions() {
		return polygonOptions;
	}
	
	public void setPolygonOptions(PolygonOptions polygonOptions) {
		this.polygonOptions = polygonOptions;
	}
}
