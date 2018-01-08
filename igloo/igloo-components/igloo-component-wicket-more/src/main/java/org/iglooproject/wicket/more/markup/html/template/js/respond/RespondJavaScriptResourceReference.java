package org.iglooproject.wicket.more.markup.html.template.js.respond;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

@Deprecated
public final class RespondJavaScriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = 1762476460042247594L;

	private static final RespondJavaScriptResourceReference INSTANCE = new RespondJavaScriptResourceReference();

	private RespondJavaScriptResourceReference() {
		super(RespondJavaScriptResourceReference.class, "respond.js");
	}

	public static RespondJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
