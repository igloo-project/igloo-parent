package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.waypoints;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class WaypointsJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -670753780950526435L;

	private static final WaypointsJavaScriptResourceReference INSTANCE = new WaypointsJavaScriptResourceReference();

	private WaypointsJavaScriptResourceReference() {
		super(WaypointsJavaScriptResourceReference.class, "jquery.waypoints.js");
	}

	public static WaypointsJavaScriptResourceReference get() {
		return INSTANCE;
	}

	@Override
	public List<HeaderItem> getPluginDependencies() {
		return Lists.newArrayListWithExpectedSize(0);
	}

}
