package fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.wicket;

import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

/*
 * wicket-ajax.js est normalement ajoute automatiquement lorsqu'un composant utilise la classe AbstractDefaultAjaxBehavior
 * see <a href="https://cwiki.apache.org/WICKET/calling-wicket-from-javascript.html"></>
 * Sa redefinition permet de l'ajouter en tant que dependance.
 */
public class WicketAjaxResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = -35072097869499823L;

	/**
	 * Singleton instance of this reference
	 */
	public static final WiQueryJavaScriptResourceReference INSTANCE = new WicketAjaxResourceReference();
	
	private WicketAjaxResourceReference() {
		super(WicketAjaxReference.class, "wicket-ajax.js");
	}
	
	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				WicketEventResourceReference.INSTANCE
		};
	}
}
