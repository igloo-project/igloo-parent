package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.jstree;

import org.odlabs.wiquery.core.resources.WiQueryStyleSheetResourceReference;

public class JstreeStyleSheetResourceReference extends WiQueryStyleSheetResourceReference {
	private static final long serialVersionUID = 1762476460042247594L;
	
	private static final JstreeStyleSheetResourceReference INSTANCE = new JstreeStyleSheetResourceReference();

	private JstreeStyleSheetResourceReference() {
		super(JstreeStyleSheetResourceReference.class, "themes/default/style.css");
	}
	
	public static JstreeStyleSheetResourceReference get() {
		return INSTANCE;
	}

}
