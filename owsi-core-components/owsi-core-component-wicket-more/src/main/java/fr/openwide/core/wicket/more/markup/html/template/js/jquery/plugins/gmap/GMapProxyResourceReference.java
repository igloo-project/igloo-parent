package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.gmap;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public final class GMapProxyResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = -4978559744127020806L;

	private static final GMapProxyResourceReference INSTANCE = new GMapProxyResourceReference();

	public GMapProxyResourceReference() {
		super(GMapProxyResourceReference.class, "jquery.gmapProxy.js");
	}

	public static GMapProxyResourceReference get() {
		return INSTANCE;
	}

}
