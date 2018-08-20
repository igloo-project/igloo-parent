package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.waypoints;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class WaypointsStickyJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -670753780950526435L;

	private static final WaypointsStickyJavaScriptResourceReference INSTANCE = new WaypointsStickyJavaScriptResourceReference();

	private WaypointsStickyJavaScriptResourceReference() {
		super(WaypointsStickyJavaScriptResourceReference.class, "sticky.js");
	}

	public static WaypointsStickyJavaScriptResourceReference get() {
		return INSTANCE;
	}

	@Override
	public List<HeaderItem> getPluginDependencies() {
		return forReferences(WaypointsJavaScriptResourceReference.get());
	}

}
