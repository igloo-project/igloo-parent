package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.alert;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public final class AlertJavascriptResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = -1811004494719333505L;

	private static final AlertJavascriptResourceReference INSTANCE = new AlertJavascriptResourceReference();

	private AlertJavascriptResourceReference() {
		super(AlertJavascriptResourceReference.class, "jquery.alert.js");
	}

	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				CoreJavaScriptResourceReference.get()
		};
	}

	public static AlertJavascriptResourceReference get() {
		return INSTANCE;
	}

}
