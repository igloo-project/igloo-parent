package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.hotkeys;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;


public final class HotkeysJavaScriptResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = -4561149170895375399L;
	
	private static final HotkeysJavaScriptResourceReference INSTANCE = new HotkeysJavaScriptResourceReference();

	private HotkeysJavaScriptResourceReference() {
		super(HotkeysJavaScriptResourceReference.class, "jquery.hotkeys.js");
	}
	
	public static HotkeysJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
