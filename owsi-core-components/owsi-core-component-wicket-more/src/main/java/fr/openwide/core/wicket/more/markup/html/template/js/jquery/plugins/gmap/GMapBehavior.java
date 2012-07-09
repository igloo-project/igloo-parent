package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

public class GMapBehavior extends WiQueryAbstractBehavior {

	private static final long serialVersionUID = -4875186513239271421L;

	public static final String EVENT_GMAP_INITIALIZED = "gmap.initialized";

	public static final String EVENT_GMAP_DRAWING_INITIALIZED = "gmap.drawingManagerInitialized";

	private GMapOptions gmapOptions;

	public GMapBehavior(GMapOptions gmapOptions) {
		super();
		this.gmapOptions = gmapOptions;
	}

	@Override
	public JsStatement statement() {
		// TODO : wiquery devrait permettre de faire un $(window).load facilement
		return new JsStatement().append("$(window)").chain(
				"load",
				JsScope.quickScope(new JsStatement().$(getComponent())
						.chain("gmap", JsUtils.quotes("init"), gmapOptions.getJavaScriptOptions())).render()
		);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(GMapResourceReference.get());
		super.renderHead(component, response);
	}

	public JsStatement setOptions(GMapOptions gmapOptions) {
		return new JsQuery(getComponent()).$().chain("gmap", JsUtils.quotes("setOptions"), gmapOptions.getJavaScriptOptions());
	}

	public void setOptions(AjaxRequestTarget ajaxRequestTarget, GMapOptions gmapOptions) {
		ajaxRequestTarget.appendJavaScript(this.setOptions(gmapOptions).render().toString());
	}

	public JsStatement createDrawingManager(DrawingManagerOptions drawingManagerOptions) {
		return new JsQuery(getComponent()).$().chain(
				"on",
				JsUtils.quotes(EVENT_GMAP_INITIALIZED),
				JsScope.quickScope(
						new JsQuery(getComponent()).$().chain("gmap", 
								JsUtils.quotes("createDrawingManager"), 
								drawingManagerOptions.getJavaScriptOptions())).render()
		);
	}

	public JsStatement setDrawingManagerOptions(DrawingManagerOptions drawingManagerOptions) {
		return new JsQuery(getComponent()).$().chain("gmap", JsUtils.quotes("setDrawingManagerOptions"), 
				drawingManagerOptions.getJavaScriptOptions());
	}

	public void setDrawingManagerOptions(AjaxRequestTarget ajaxRequestTarget, DrawingManagerOptions drawingManagerOptions) {
		ajaxRequestTarget.appendJavaScript(this.setDrawingManagerOptions(drawingManagerOptions).render().toString());
	}

	public JsStatement setMapEnabled(boolean enabled) {
		return new JsQuery(getComponent()).$().chain("gmap", JsUtils.quotes("setMapEnabled"),
				Boolean.toString(enabled));
	}

	public void setMapEnabled(AjaxRequestTarget ajaxRequestTarget, boolean enabled) {
		ajaxRequestTarget.appendJavaScript(this.setMapEnabled(enabled).render().toString());
	}

	public JsStatement setShapesEditable(boolean editable) {
		return new JsQuery(getComponent()).$().chain("gmap", JsUtils.quotes("setShapesEditable"),
				Boolean.toString(editable));
	}

	public void setShapesEditable(AjaxRequestTarget ajaxRequestTarget, boolean editable) {
		ajaxRequestTarget.appendJavaScript(this.setShapesEditable(editable).render().toString());
	}

	public JsStatement clearShapes() {
		return new JsQuery(getComponent()).$().chain("gmap", JsUtils.quotes("clearShapes"));
	}

	public void clearShapes(AjaxRequestTarget ajaxRequestTarget) {
		ajaxRequestTarget.appendJavaScript(this.clearShapes().render().toString());
	}

	//	Preferences helpers
	public JsStatement bindBoundsChanged(String gmapId, boolean bind,
			String longitudeInputId, String latitudeInputId, String zoomInputId) {
		if (gmapId == null || longitudeInputId == null || latitudeInputId == null || zoomInputId == null) {
			throw new IllegalStateException("bindBoundsChanged : valeurs nulles.");
		}
		
		return new JsQuery(getComponent()).$().chain("gmaphelper.bindBoundsChanged", 
				JsUtils.quotes(gmapId), Boolean.toString(bind),
				JsUtils.quotes(longitudeInputId), JsUtils.quotes(latitudeInputId), JsUtils.quotes(zoomInputId));
	}

	public void bindBoundsChanged(AjaxRequestTarget ajaxRequestTarget, String gmapId, boolean bind,
			String longitudeInputId, String latitudeInputId, String zoomInputId) {
		ajaxRequestTarget.appendJavaScript(this.bindBoundsChanged(gmapId, bind, longitudeInputId, latitudeInputId, zoomInputId).render().toString());
	}

	public JsStatement bindMapTypeIdChanged(String gmapId, boolean bind, String inputId) {
		if (gmapId == null || inputId == null) {
			throw new IllegalStateException("bindMapTypeIdChanged : valeurs nulles.");
		}
		
		return new JsQuery(getComponent()).$().chain("gmaphelper.bindMapTypeIdChanged", 
				JsUtils.quotes(gmapId), Boolean.toString(bind), JsUtils.quotes(inputId));
	}

	public void bindMapTypeIdChanged(AjaxRequestTarget ajaxRequestTarget, String gmapId, boolean bind, String inputId) {
		ajaxRequestTarget.appendJavaScript(this.bindMapTypeIdChanged(gmapId, bind, inputId).render().toString());
	}

	public JsStatement bindPolygonComplete(String gmapId, boolean bind, String inputId) {
		if (gmapId == null || inputId == null) {
			throw new IllegalStateException("bindPolygonComplete : valeurs nulles.");
		}
		
		return new JsQuery(getComponent()).$().chain("gmaphelper.bindPolygonComplete", 
				JsUtils.quotes(gmapId), Boolean.toString(bind), JsUtils.quotes(inputId));
	}

	public void bindPolygonComplete(AjaxRequestTarget ajaxRequestTarget, String gmapId, boolean bind, String inputId) {
		ajaxRequestTarget.appendJavaScript(this.bindPolygonComplete(gmapId, bind, inputId).render().toString());
	}
}
