package igloo.select2;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class Select2JavaScriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = -4407129204421497333L;

	private static final Select2JavaScriptResourceReference INSTANCE = new Select2JavaScriptResourceReference();

	private Select2JavaScriptResourceReference() {
		super(Select2JavaScriptResourceReference.class, "select2.full.js");
	}

	public static Select2JavaScriptResourceReference get() {
		return INSTANCE;
	}

}
