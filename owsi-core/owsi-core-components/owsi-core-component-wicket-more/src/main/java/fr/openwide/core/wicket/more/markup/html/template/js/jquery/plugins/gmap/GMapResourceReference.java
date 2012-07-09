package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop.ScrollToTopJavaScriptResourceReference;

public final class GMapResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = -6438523251568468748L;

	public static final GMapResourceReference INSTANCE = new GMapResourceReference();

	private GMapResourceReference() {
		super(GMapResourceReference.class, "jquery.gmap.js");
	}

	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				GMapProxyResourceReference.get(),
				ScrollToTopJavaScriptResourceReference.get()
		};
	}

	public static GMapResourceReference get() {
		return INSTANCE;
	}
	
}
