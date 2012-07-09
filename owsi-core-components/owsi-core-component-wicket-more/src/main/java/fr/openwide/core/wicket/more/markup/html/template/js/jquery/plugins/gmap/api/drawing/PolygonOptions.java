package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.drawing;

import org.odlabs.wiquery.core.options.Options;

public class PolygonOptions extends Options {

	private static final long serialVersionUID = -7596540081861463771L;
	
	private Boolean editable;
	
	private Boolean visible;
	
	public PolygonOptions(Boolean editable) {
		this.editable = editable;
	}

	@Override
	public CharSequence getJavaScriptOptions() {
		if (editable != null) {
			put("editable", editable);
		}
		if (visible != null) {
			put("visible", visible);
		}
		
		return super.getJavaScriptOptions();
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
}
