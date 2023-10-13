package igloo.bootstrap5.markup.html.template.js.bootstrap;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class Bootstrap5JavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final Bootstrap5JavaScriptResourceReference INSTANCE = new Bootstrap5JavaScriptResourceReference();

	private Bootstrap5JavaScriptResourceReference() {
		super("bootstrap5-override/current/js/dist/bootstrap.js");
	}

	public static Bootstrap5JavaScriptResourceReference get() {
		return INSTANCE;
	}

}
