package org.iglooproject.wicket.more.markup.html.template.js.clipboard;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class ClipboardJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = -4307688306700113307L;

	private static final ClipboardJavaScriptResourceReference INSTANCE = new ClipboardJavaScriptResourceReference();

	private ClipboardJavaScriptResourceReference() {
		super("webjars/clipboard/current/dist/clipboard.js");
	}

	public static ClipboardJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
