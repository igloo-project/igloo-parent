package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.map;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.infowindow.InfoBubbleResourceReference;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.wicket.WicketAjaxGmapResourceReference;

public class GmapResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = -6438523251568468748L;

	public static final WiQueryJavaScriptResourceReference INSTANCE = new GmapResourceReference();
	
	private GmapResourceReference() {
		super(GMapOptions.class, "jquery.gmap.js");
	}
	
	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				InfoBubbleResourceReference.get(),
				WicketAjaxGmapResourceReference.get()
		};
	}
}
