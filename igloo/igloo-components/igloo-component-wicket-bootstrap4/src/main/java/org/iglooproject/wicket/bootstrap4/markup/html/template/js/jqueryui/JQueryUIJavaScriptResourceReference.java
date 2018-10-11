package org.iglooproject.wicket.bootstrap4.markup.html.template.js.jqueryui;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.resource.JQueryPluginResourceReference;
import org.wicketstuff.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

/**
 * References the full jQuery UI library <strong>WITHOUT</strong> Tooltip Widget (conflict with Bootstrap Tooltip).
 */
public class JQueryUIJavaScriptResourceReference extends JQueryPluginResourceReference
{
	private static final long serialVersionUID = 4585057795574929263L;

	private static final JQueryUIJavaScriptResourceReference INSTANCE = new JQueryUIJavaScriptResourceReference();

	public static JQueryUIJavaScriptResourceReference get()
	{
		return INSTANCE;
	}

	/**
	 * Builds a new instance of {@link JQueryUIJavaScriptResourceReference}.
	 */
	protected JQueryUIJavaScriptResourceReference()
	{
		super(JQueryUIJavaScriptResourceReference.class, "jquery-ui.js");
	}

	@Override
	public List<HeaderItem> getDependencies()
	{
		List<HeaderItem> jquery = super.getDependencies();
		jquery.add(CssHeaderItem.forReference(WiQueryCoreThemeResourceReference.get()));
		return jquery;
	}
}
