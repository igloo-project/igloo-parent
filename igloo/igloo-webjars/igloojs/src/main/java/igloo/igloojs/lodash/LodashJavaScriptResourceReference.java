package igloo.igloojs.lodash;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public class LodashJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = -7842833159411094634L;

	private static final LodashJavaScriptResourceReference INSTANCE = new LodashJavaScriptResourceReference();

	private LodashJavaScriptResourceReference() {
		super("lodash/current/lodash.js");
	}

	public static LodashJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
