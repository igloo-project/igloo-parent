package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.sortable;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;


public final class SortableListUpdateJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = -4241064241040182943L;

	private static final SortableListUpdateJavaScriptResourceReference INSTANCE = new SortableListUpdateJavaScriptResourceReference();

	private SortableListUpdateJavaScriptResourceReference() {
		super(SortableListUpdateJavaScriptResourceReference.class, "sortableListUpdate.js");
	}

	public static SortableListUpdateJavaScriptResourceReference get() {
		return INSTANCE;
	}

}