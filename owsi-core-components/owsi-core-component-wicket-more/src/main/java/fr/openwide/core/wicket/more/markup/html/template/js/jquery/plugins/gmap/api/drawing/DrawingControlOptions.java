package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.drawing;

import java.util.List;

import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.util.GJsStatementUtils;

public class DrawingControlOptions extends Options {
	
	private static final long serialVersionUID = -3465669986675757734L;
	
	private GControlPosition position;
	
	private List<GOverlayType> drawingModes;
	
	public DrawingControlOptions(GControlPosition position, List<GOverlayType> drawingModes) {
		this.position = position;
		this.drawingModes = drawingModes;
	}
	
	@Override
	public CharSequence getJavaScriptOptions() {
		if (position != null) {
			put("position", GJsStatementUtils.getJavaScriptStatement(position));
		}
		if (drawingModes != null) {
			StringBuilder drawingModesSB = new StringBuilder("new Array(");
			int nbModes = drawingModes.size();
			for (int i=0; i<nbModes; i++) {
				if (i>0) {
					drawingModesSB.append(", ");
				}
				drawingModesSB.append(GJsStatementUtils.getJavaScriptStatement(drawingModes.get(i)));
			}
			drawingModesSB.append(")");
			put("drawingModes", drawingModesSB.toString());
		}
		
		return super.getJavaScriptOptions();
	}
	
	public GControlPosition getPosition() {
		return position;
	}
	
	public void setPosition(GControlPosition position) {
		this.position = position;
	}
	
	public List<GOverlayType> getDrawingModes() {
		return drawingModes;
	}
	
	public void setDrawingModes(List<GOverlayType> drawingModes) {
		this.drawingModes = drawingModes;
	}
}