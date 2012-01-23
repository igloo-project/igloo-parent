package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.wicket;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

import fr.openwide.core.wicket.gmap.js.jquery.plugins.json.JsonResourceReference;

public class WicketAjaxGmapResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = 8028339323586140527L;
	
	/**
	 * Singleton instance of this reference
	 */
	public static final WiQueryJavaScriptResourceReference INSTANCE = new WicketAjaxGmapResourceReference();
	
	private WicketAjaxGmapResourceReference() {
		super(WicketAjaxGmapResourceReference.class, "wicket-ajax-gmap.js");
	}
	
	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
			WicketAjaxResourceReference.INSTANCE,
			JsonResourceReference.INSTANCE
		};
	}
}
