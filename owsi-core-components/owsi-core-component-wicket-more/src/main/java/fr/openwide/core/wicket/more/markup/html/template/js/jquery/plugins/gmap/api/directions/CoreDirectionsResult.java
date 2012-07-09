package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap.api.directions;

import java.io.Serializable;

public class CoreDirectionsResult implements Serializable {
	private static final long serialVersionUID = -6035369531710707999L;

	private String status;
	private String polyline;
	
	public CoreDirectionsResult(){
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPolyline() {
		return polyline;
	}

	public void setPolyline(String polyline) {
		this.polyline = polyline;
	}
}
