package igloo.select2;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class Select2MoreJavaScriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = -7035932997722320946L;

	private static final Select2MoreJavaScriptResourceReference INSTANCE = new Select2MoreJavaScriptResourceReference();

	private Select2MoreJavaScriptResourceReference() {
		super(Select2MoreJavaScriptResourceReference.class, "select2-more.js");
	}

	public static Select2MoreJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
