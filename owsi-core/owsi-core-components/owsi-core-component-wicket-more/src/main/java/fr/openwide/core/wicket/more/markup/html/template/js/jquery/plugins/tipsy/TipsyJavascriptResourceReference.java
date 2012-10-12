package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;


public final class TipsyJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 304303778975104796L;

	private static final TipsyJavascriptResourceReference INSTANCE = new TipsyJavascriptResourceReference();

	private TipsyJavascriptResourceReference() {
		super(TipsyJavascriptResourceReference.class, "jquery.tipsy.js");
	}

	public static TipsyJavascriptResourceReference get() {
		return INSTANCE;
	}

}
