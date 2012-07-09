package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.drawing.GControlPosition;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.drawing.GOverlayType;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.util.GJsStatementUtils;

/*
 * see <a href="https://developers.google.com/maps/documentation/javascript/reference#DrawingManagerOptions"></a>
 */
public class CreateDrawingManager implements ChainableStatement, Serializable {
	private static final long serialVersionUID = -1661070056021679177L;

	private boolean drawingControl;

	private GControlPosition controlPosition;

	private GOverlayType overlayType;

	public CreateDrawingManager(boolean drawingControl, GControlPosition controlPosition) {
		this.drawingControl = drawingControl;
		this.controlPosition = controlPosition;
	}

	@Override
	public String chainLabel() {
		return "gmap";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		options.put("drawingControl", drawingControl);
		
		if (controlPosition != null) {
			options.put("controlPosition", GJsStatementUtils.getJavaScriptStatement(controlPosition));
		}
		if (overlayType != null) {
			options.put("overlayType", GJsStatementUtils.getJavaScriptStatement(overlayType));
		}
		
		CharSequence[] args = new CharSequence[2];
		args[0] = JsUtils.quotes("createDrawingManager");
		args[1] = options.getJavaScriptOptions();
		return args;
	}

	public boolean isDrawingControl() {
		return drawingControl;
	}

	public void setDrawingControl(boolean drawingControl) {
		this.drawingControl = drawingControl;
	}

	public GControlPosition getControlPosition() {
		return controlPosition;
	}

	public void setControlPosition(GControlPosition controlPosition) {
		this.controlPosition = controlPosition;
	}

	public GOverlayType getOverlayType() {
		return overlayType;
	}

	public void setOverlayType(GOverlayType overlayType) {
		this.overlayType = overlayType;
	}
}
