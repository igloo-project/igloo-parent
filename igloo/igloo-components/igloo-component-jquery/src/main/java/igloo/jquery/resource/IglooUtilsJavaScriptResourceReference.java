package igloo.jquery.resource;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public final class IglooUtilsJavaScriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = 1762476460042247594L;

	private static final IglooUtilsJavaScriptResourceReference INSTANCE = new IglooUtilsJavaScriptResourceReference();

	private IglooUtilsJavaScriptResourceReference() {
		super(IglooUtilsJavaScriptResourceReference.class, "igloo.utils.js");
	}

	public static IglooUtilsJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
