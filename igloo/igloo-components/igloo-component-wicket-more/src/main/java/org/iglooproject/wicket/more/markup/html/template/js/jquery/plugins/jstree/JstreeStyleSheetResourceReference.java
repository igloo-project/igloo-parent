package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.jstree;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class JstreeStyleSheetResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = 1762476460042247594L;
	
	private static final JstreeStyleSheetResourceReference INSTANCE = new JstreeStyleSheetResourceReference();

	private JstreeStyleSheetResourceReference() {
		super(JstreeStyleSheetResourceReference.class, "themes/default/style.css");
	}
	
	public static JstreeStyleSheetResourceReference get() {
		return INSTANCE;
	}

}
