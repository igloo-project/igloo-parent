package fr.openwide.core.wicket.more.core.resources;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public final class Core172JavaScriptResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = 2035263642593690911L;

	private static final Core172JavaScriptResourceReference INSTANCE = new Core172JavaScriptResourceReference();

	private Core172JavaScriptResourceReference() {
		super(Core172JavaScriptResourceReference.class, "jquery-1.7.2.js");
	}

	public static Core172JavaScriptResourceReference get() {
		return INSTANCE;
	}

}
