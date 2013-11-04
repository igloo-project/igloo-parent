package fr.openwide.core.wicket.more.markup.html.template.flash.zeroclipboard;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public final class ZeroClipboardJavascriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = 7052008970105566948L;

	private static final ZeroClipboardJavascriptResourceReference INSTANCE = new ZeroClipboardJavascriptResourceReference();

	private ZeroClipboardJavascriptResourceReference() {
		super(ZeroClipboardJavascriptResourceReference.class, "ZeroClipboard.js");
	}

	public static ZeroClipboardJavascriptResourceReference get() {
		return INSTANCE;
	}

}
