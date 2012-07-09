package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

/**
 * {@link ChainableStatement} qui permet d'appeler la méthode addMarkersFromMarkup du plugin
 * gmap.
 */
public class AddMarkersFromMarkup implements ChainableStatement, Serializable {

	private static final long serialVersionUID = -6631445544001018570L;

	private String selector;

	private Options markerOptions = new Options();

	private boolean autofit = false;

	private boolean geocode = false;

	private String geocodeSelector;

	private int geocodeZoom;


	/**
	 * {@link ChainableStatement} qui permet d'appeler la méthode addMarkersFromMarkup du plugin
	 * gmap.
	 * 
	 * @param selector : le sélecteur à appliquer pour trouver les éléments à afficher. Ces éléments doivent
	 *                   comporter les informations sur l'objet dans des attributs data-gmap-*.
	 * @param markerOptions : les options par défaut à appliquer sur le marqueur ; les autre options sont déterminées
	 *                        via les attributes data-gmap-*
	 * @param autofit : indique s'il faut réaliser un autofit après avoir posé les marqueurs.
	 * @param geocodeSelector : indique sur quel élément faire la géolocalisation pour centrer la carte dans le cas
	 *                          où aucune donnée de géolocalisation n'est disponible.
	 * @param geocodeSelector : indique quel zoom utiliser avec la géolocalisation pour centrer la carte dans le cas
	 *                          où aucune donnée de géolocalisation n'est disponible.
	 */
	public AddMarkersFromMarkup(String selector, Options markerOptions, boolean autofit, String geocodeSelector, int geocodeZoom) {
		this(selector, markerOptions, autofit);
		this.geocode = true;
		this.geocodeSelector = geocodeSelector;
		this.geocodeZoom = geocodeZoom;
	}

	public AddMarkersFromMarkup(String selector, Options markerOptions, boolean autofit) {
		super();
		this.selector = selector;
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
		
		if (geocode) {
			return new String[] { JsUtils.quotes("addMarkersFromMarkup"),
					selector,
					markerOptions.getJavaScriptOptions().toString(),
					Boolean.toString(autofit),
					Boolean.toString(geocode),
					geocodeSelector,
					Integer.toString(geocodeZoom)
			};
		} else {
			return new String[] { JsUtils.quotes("addMarkersFromMarkup"),
					selector,
					markerOptions.getJavaScriptOptions().toString(),
					Boolean.toString(autofit),
			};
		}
	}

}
