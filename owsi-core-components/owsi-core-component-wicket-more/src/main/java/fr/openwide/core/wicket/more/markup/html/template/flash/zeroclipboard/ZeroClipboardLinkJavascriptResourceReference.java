package fr.openwide.core.wicket.more.markup.html.template.flash.zeroclipboard;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public final class ZeroClipboardLinkJavascriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = 5717209494409432648L;

	private static final ZeroClipboardLinkJavascriptResourceReference INSTANCE = new ZeroClipboardLinkJavascriptResourceReference();

	private ZeroClipboardLinkJavascriptResourceReference() {
		super(ZeroClipboardLinkJavascriptResourceReference.class, "ZeroClipboardLink.js");
	}

	public static ZeroClipboardLinkJavascriptResourceReference get() {
		return INSTANCE;
	}

}
