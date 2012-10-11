package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.alert;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryJavaScriptResourceReference;

public final class AlertJavascriptResourceReference extends JQueryJavaScriptResourceReference {

	private static final long serialVersionUID = -1811004494719333505L;

	private static final AlertJavascriptResourceReference INSTANCE = new AlertJavascriptResourceReference();

	private AlertJavascriptResourceReference() {
		super(AlertJavascriptResourceReference.class, "jquery.alert.js");
	}

	public static AlertJavascriptResourceReference get() {
		return INSTANCE;
	}
}
