package fr.openwide.core.wicket.gmap.api.event;

import java.io.Serializable;

public enum GMarkerEvent implements Serializable {

	ANIMATION_CHANGED("animation_changed"),
	CLICK("click"),
	CLICKABLE_CHANGED("clickable_changed"),
	CURSOR_CHANGED("cursor_changed"),
	DBLCLICK("dbclick"),
	DRAG("drag"),
	DRAGEND("dragend"),
	DRAGGABLE_CHANGED("draggable_changed"),
	DRAGSTART("dragstart"),
	FLAT_CHANGED("flat_changed"),
	ICON_CHANGED("icon_changed"),
	MOUSEDOWN("mousedown"),
	MOUSEOUT("mouseout"),
	MOUSEOVER("mouseover"),
	MOUSEUP("mouseup"),
	POSITION_CHANGED("position_changed"),
	RIGHTCLICK("rightclick"),
	SHADOW_CHANGED("shadow_changed"),
	SHAPE_CHANGED("shape_changed"),
	TITLE_CHANGED("title_changed"),
	VISIBLE_CHANGED("visible_changed"),
	ZINDEX_CHANGED("zindex_changed");
	
	private String value;
	
	private GMarkerEvent(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
