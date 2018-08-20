package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.json;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;


public final class JsonJavascriptResourceReference extends WebjarsJavaScriptResourceReference {
	private static final long serialVersionUID = 1L;

	private static final JsonJavascriptResourceReference INSTANCE = new JsonJavascriptResourceReference();

	private JsonJavascriptResourceReference() {
		super("jquery.json/current/jquery.json-2.3.js");
	}

	public static JsonJavascriptResourceReference get() {
		return INSTANCE;
	}

}
