package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

/**
 * {@link ChainableStatement} qui permet d'appeler la méthode toggleMarkersFromMarkup du plugin
 * gmap.
 */
public class ToggleMarkersFromMarkup implements ChainableStatement, Serializable {

	private static final long serialVersionUID = -6631445544001018570L;

	private String selector;

	private Options markerOptions = new Options();

	private boolean autofit = false;

	private String show;

	/**
	 * {@link ChainableStatement} qui permet d'appeler la méthode toggleMarkersFromMarkup du plugin
	 * gmap.
	 * 
	 * @param selector : le sélecteur à appliquer pour trouver les éléments à afficher. Ces éléments doivent
	 *                   comporter les informations sur l'objet dans des attributs data-gmap-*.
	 * @param show : affiche les marqueurs si true, les supprime sinon ; peut être une expression javascript
	 * @param markerOptions : les options par défaut à appliquer sur le marqueur ; les autre options sont déterminées
	 *                        via les attributes data-gmap-*
	 * @param autofit : indique s'il faut réaliser un autofit après avoir posé les marqueurs.
	 */
	public ToggleMarkersFromMarkup(String selector, String show, Options markerOptions, boolean autofit) {
		super();
		this.selector = selector;
		this.show = show;
		this.markerOptions = markerOptions;
		this.autofit = autofit;
	}

	@Override
	public String chainLabel() {
		return "gmap";
	}

	@Override
	public CharSequence[] statementArgs() {
		if (selector == null || markerOptions == null) {
			throw new IllegalStateException("Le sélecteur et les options sont obligatoires.");
		}
		
		return new String[] { JsUtils.quotes("toggleMarkersFromMarkup"), selector, show, markerOptions.getJavaScriptOptions().toString(), Boolean.toString(autofit) };
	}

}
