package fr.openwide.core.wicket.more.markup.html.template.js.owsi;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class OwsiUtilsJavaScriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = 1762476460042247594L;

	private static final OwsiUtilsJavaScriptResourceReference INSTANCE = new OwsiUtilsJavaScriptResourceReference();

	private OwsiUtilsJavaScriptResourceReference() {
		super(OwsiUtilsJavaScriptResourceReference.class, "owsi.utils.js");
	}

	public static OwsiUtilsJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
