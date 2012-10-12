package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.jstree;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public class JstreeJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 1762476460042247594L;

	private static final JstreeJavaScriptResourceReference INSTANCE = new JstreeJavaScriptResourceReference();

	private JstreeJavaScriptResourceReference() {
		super(JstreeJavaScriptResourceReference.class, "jquery.jstree.js");
	}

	public static JstreeJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
