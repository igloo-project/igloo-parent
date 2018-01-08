package org.iglooproject.wicket.more.markup.html.template.js.popper;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public final class PopperJavaScriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = 1762476460042247594L;

	private static final PopperJavaScriptResourceReference INSTANCE = new PopperJavaScriptResourceReference();

	private PopperJavaScriptResourceReference() {
		super(PopperJavaScriptResourceReference.class, "popper.js");
	}

	public static PopperJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
