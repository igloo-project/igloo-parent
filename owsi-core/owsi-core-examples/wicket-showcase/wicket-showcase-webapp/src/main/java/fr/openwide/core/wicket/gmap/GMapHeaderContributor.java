package fr.openwide.core.wicket.gmap;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

import fr.openwide.core.wicket.gmap.api.version.GMapVersion;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.GMapOptions;

public class GMapHeaderContributor extends Behavior {
	private static final long serialVersionUID = 417580948999506226L;

	// URL for Google Maps' API endpoint.
	private static final String GMAP_API_URL = "http://maps.googleapis.com/maps/api/js?sensor=false";
	
	private static final WiQueryJavaScriptResourceReference WICKET_GMAP_JS = new WiQueryJavaScriptResourceReference(GMapOptions.class, "jquery.gmap.js"){
		private static final long serialVersionUID = -1536497398319819956L;

		@Override
		public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
			return new AbstractResourceDependentResourceReference[] {
					CoreJavaScriptResourceReference.get()};
		}
	};
	
	/*
	 * The region parameter accepts Unicode region subtag identifiers
	 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/basics.html#Localization">
	 */
	private String region;
	
	/*
	 * Supported Language
	 * see <a href="https://spreadsheets.google.com/pub?key=p9pdwsai2hDMsLkXsoM05KQ&gid=1">
	 */
	private Locale locale;
	
	/*
	 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/basics.html#Versioning">
	 */
	private GMapVersion version;
	
	public GMapHeaderContributor(){
		this(null, null, null);
	}
	
	public GMapHeaderContributor(String region, Locale locale) {
		this(region, locale, null);
	}
	
	public GMapHeaderContributor(String region, Locale locale, GMapVersion version) {
		this.region = region;
		this.locale = locale;
		this.version = version;
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		StringBuilder completeGmapApiUrl = new StringBuilder(GMAP_API_URL);
		
		if (region != null) {
			completeGmapApiUrl.append("&region=" + region);
		}
		if (locale != null) {
			completeGmapApiUrl.append("&language=" + locale.getLanguage());
		}
		if (version != null) {
			completeGmapApiUrl.append("&v=" + version.getValue());
		}

		response.renderJavaScriptReference(completeGmapApiUrl.toString());
		response.renderJavaScriptReference(WICKET_GMAP_JS);
	}
}
