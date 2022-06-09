package igloo.bootstrap4.markup.html.template.js.sidebar;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;

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
