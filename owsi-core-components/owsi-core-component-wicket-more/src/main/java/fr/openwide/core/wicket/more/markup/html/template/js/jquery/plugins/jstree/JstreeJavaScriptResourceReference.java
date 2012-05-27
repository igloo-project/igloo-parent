package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.jstree;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public class JstreeJavaScriptResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = 1762476460042247594L;
	
	private static final JstreeJavaScriptResourceReference INSTANCE = new JstreeJavaScriptResourceReference();

	public JstreeJavaScriptResourceReference() {
		super(JstreeJavaScriptResourceReference.class, "jquery.jstree.js");
	}
	
	public static JstreeJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
