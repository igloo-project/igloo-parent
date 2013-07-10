package fr.openwide.core.wicket.resource;

import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;

public class JQuery183ResourceReference extends JavaScriptResourceReference {
	private static final long serialVersionUID = 1L;

	public static final String VERSION_1 = "jquery/jquery-1.8.3.js";

	private static final JQuery183ResourceReference INSTANCE = new JQuery183ResourceReference();

	/**
	 * Normally you should not use this method, but use
	 * {@link IJavaScriptLibrarySettings#getJQueryReference()} to prevent
	 * version conflicts.
	 * 
	 * @return the single instance of the resource reference
	 */
	public static JQuery183ResourceReference get() {
		return INSTANCE;
	}

	protected JQuery183ResourceReference() {
		super(JQuery183ResourceReference.class, VERSION_1);
	}
}
