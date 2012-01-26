package fr.openwide.core.wicket.gmap.api.event;

import java.io.Serializable;

public enum GMapEvent implements Serializable {

	BOUNDS_CHANGED("bounds_changed"),
	CENTER_CHANGED("center_changed"),
	CLICK("click"),
	DBLCLICK("dbclick"),
	DRAG("drag"),
	DRAGEND("dragend"),
	DRAGSTART("dragstart"),
	HEADING_CHANGED("heading_changed"),
	IDLE("idle"),
	MAPTYPEID_CHANGED("maptypeid_changed"),
	MOUSEMOVE("mousemove"),
	MOUSEOUT("mouseout"),
	MOUSEOVER("mouseover"),
	PROJECTION_CHANGED("projection_changed"),
	RESIZE("resize"),
	RIGHTCLICK("rightclick"),
	TILESLOADED("tilesloaded"),
	TILT_CHANGED("tilt_changed"),
	ZOOM_CHANGED("zoom_changed");
	
	private String value;
	
	private GMapEvent(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
