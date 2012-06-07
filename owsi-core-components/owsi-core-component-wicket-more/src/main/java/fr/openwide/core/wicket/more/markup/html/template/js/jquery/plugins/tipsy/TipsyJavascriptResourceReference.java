package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public final class TipsyJavascriptResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = 304303778975104796L;
	
	private static final TipsyJavascriptResourceReference INSTANCE = new TipsyJavascriptResourceReference();

	private TipsyJavascriptResourceReference() {
		super(TipsyJavascriptResourceReference.class, "jquery.tipsy.js");
	}

	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				CoreJavaScriptResourceReference.get()
		};
	}
	
	public static TipsyJavascriptResourceReference get() {
		return INSTANCE;
	}

}
