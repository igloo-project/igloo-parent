package org.iglooproject.wicket.jqueryui;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.resource.JQueryPluginResourceReference;
import org.wicketstuff.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

public class JQueryUIJavaScriptResourceReference extends JQueryPluginResourceReference {

	private static final long serialVersionUID = 7176658913755138657L;

	private static final JQueryUIJavaScriptResourceReference INSTANCE = new JQueryUIJavaScriptResourceReference();

	public static JQueryUIJavaScriptResourceReference get() {
		return INSTANCE;
	}

	protected JQueryUIJavaScriptResourceReference() {
		super(JQueryUIJavaScriptResourceReference.class, "jquery-ui.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(CssHeaderItem.forReference(WiQueryCoreThemeResourceReference.get()));
		return dependencies;
	}

}
