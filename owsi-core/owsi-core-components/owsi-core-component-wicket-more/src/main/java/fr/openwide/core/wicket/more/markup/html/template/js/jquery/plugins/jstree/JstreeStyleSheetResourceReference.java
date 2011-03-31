package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.jstree;

import org.odlabs.wiquery.core.commons.compressed.WiQueryYUICompressedStyleSheetResourceReference;

public class JstreeStyleSheetResourceReference extends WiQueryYUICompressedStyleSheetResourceReference {
	private static final long serialVersionUID = 1762476460042247594L;
	
	private static final JstreeStyleSheetResourceReference INSTANCE = new JstreeStyleSheetResourceReference();

	public JstreeStyleSheetResourceReference() {
		super(JstreeStyleSheetResourceReference.class, "themes/default/style.css");
	}
	
	public static JstreeStyleSheetResourceReference get() {
		return INSTANCE;
	}

}
