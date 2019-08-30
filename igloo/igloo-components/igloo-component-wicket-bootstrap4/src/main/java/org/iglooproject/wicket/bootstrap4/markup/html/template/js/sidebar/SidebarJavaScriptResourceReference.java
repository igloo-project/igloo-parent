package org.iglooproject.wicket.bootstrap4.markup.html.template.js.sidebar;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class SidebarJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 4245186847401300556L;

	private static final SidebarJavaScriptResourceReference INSTANCE = new SidebarJavaScriptResourceReference();

	private SidebarJavaScriptResourceReference() {
		super(SidebarJavaScriptResourceReference.class, "sidebar.js");
	}

	public static SidebarJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
