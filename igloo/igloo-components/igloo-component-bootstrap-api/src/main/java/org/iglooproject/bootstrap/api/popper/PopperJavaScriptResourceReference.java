package org.iglooproject.bootstrap.api.popper;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class PopperJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = 1762476460042247594L;

	private static final PopperJavaScriptResourceReference INSTANCE = new PopperJavaScriptResourceReference();

	private PopperJavaScriptResourceReference() {
		super("popperjs__core/current/dist/umd/popper.js");
	}

	public static PopperJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
