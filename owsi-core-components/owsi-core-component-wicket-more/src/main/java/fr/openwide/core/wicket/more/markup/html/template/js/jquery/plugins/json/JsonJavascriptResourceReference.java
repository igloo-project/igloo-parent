package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.json;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public class JsonJavascriptResourceReference extends WiQueryJavaScriptResourceReference {
		private static final long serialVersionUID = 1L;

	/**
	 * Singleton instance of this reference
	 */
	public static final WiQueryJavaScriptResourceReference INSTANCE = new JsonJavascriptResourceReference();

	private JsonJavascriptResourceReference() {
		super(JsonJavascriptResourceReference.class, "jquery.json-2.3.js");
	}

	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				CoreJavaScriptResourceReference.get()
		};
	}
}
