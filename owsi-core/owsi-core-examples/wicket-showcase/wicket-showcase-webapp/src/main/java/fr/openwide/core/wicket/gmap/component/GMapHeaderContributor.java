package fr.openwide.core.wicket.gmap.component;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;

import fr.openwide.core.wicket.gmap.api.version.GMapVersion;
import fr.openwide.core.wicket.gmap.js.jquery.plugins.gmap.map.GmapResourceReference;

public class GMapHeaderContributor extends Behavior {
	private static final long serialVersionUID = 417580948999506226L;

	// URL for Google Maps' API endpoint.
	private static final String GMAP_API_URL = "http://maps.googleapis.com/maps/api/js?sensor=false";
	
	/*
	 * The region parameter accepts Unicode region subtag identifiers
	 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/basics.html#Localization">
	 */
	private Locale region;
	
	/*
	 * Supported Language
	 * see <a href="https://spreadsheets.google.com/pub?key=p9pdwsai2hDMsLkXsoM05KQ&gid=1">
	 */
	private Locale locale;
	
	/*
	 * see <a href="http://code.google.com/intl/fr-FR/apis/maps/documentation/javascript/basics.html#Versioning">
	 */
	private GMapVersion version;
	
	public GMapHeaderContributor() {
		this(null, null, null);
	}
	
	public GMapHeaderContributor(Locale region) {
		this(region, null, null);
	}
	
	public GMapHeaderContributor(Locale region, Locale locale) {
		this(region, locale, null);
	}
	
	public GMapHeaderContributor(Locale region, Locale locale, GMapVersion version) {
		if (region == null) {
			this.region = Locale.FRANCE;
		} else {
			this.region = region;
		}
		
		if (locale == null && Session.exists()) {
			this.locale = Session.get().getLocale();
		} else {
			this.locale = locale;
		}
		
		if (version == null) {
			this.version = GMapVersion.CURRENT;
		} else {
			this.version = version;
		}
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		StringBuilder completeGmapApiUrl = new StringBuilder(GMAP_API_URL);
		
		if (region != null) {
			completeGmapApiUrl.append("&region=");
			completeGmapApiUrl.append(region.getCountry());
		}
		if (locale != null) {
			completeGmapApiUrl.append("&language=");
			completeGmapApiUrl.append(locale.getLanguage());
		}
		if (version != null) {
			completeGmapApiUrl.append("&v=");
			completeGmapApiUrl.append(version.getValue());
		}

		response.renderJavaScriptReference(completeGmapApiUrl.toString());
		response.renderJavaScriptReference(GmapResourceReference.INSTANCE);
	}
}