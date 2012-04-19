package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;


public final class EasingJavaScriptResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = -7107928462642160157L;
	
	private static final EasingJavaScriptResourceReference INSTANCE = new EasingJavaScriptResourceReference();

	private EasingJavaScriptResourceReference() {
		super(EasingJavaScriptResourceReference.class, "jquery.easing.1.3.js");
	}
	
	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				CoreJavaScriptResourceReference.get()
		};
	}
	
	public static EasingJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
