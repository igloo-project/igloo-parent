package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tab;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public final class BootstrapTabJavascriptResourceReference extends WiQueryJavaScriptResourceReference {
	
	private static final long serialVersionUID = 4895439022427021364L;
	
	private static final BootstrapTabJavascriptResourceReference INSTANCE = new BootstrapTabJavascriptResourceReference();
	
	private BootstrapTabJavascriptResourceReference() {
		super(BootstrapTabJavascriptResourceReference.class, "bootstrap-tab.js");
	}

	public static BootstrapTabJavascriptResourceReference get() {
		return INSTANCE;
	}

}
