package org.iglooproject.wicket.more.markup.html.template.js.popper;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class PopperJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = 1762476460042247594L;

	private static final PopperJavaScriptResourceReference INSTANCE = new PopperJavaScriptResourceReference();

	private PopperJavaScriptResourceReference() {
		super("webjars/popper.js/current/dist/umd/popper.js");
	}

	public static PopperJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
