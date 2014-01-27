package fr.openwide.core.wicket.resource;

import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;

public class JQueryUpdateResourceReference extends JavaScriptResourceReference {
	private static final long serialVersionUID = 1L;

	public static final String VERSION_1 = "jquery/jquery-1.11.0.js";

	private static final JQueryUpdateResourceReference INSTANCE = new JQueryUpdateResourceReference();

	/**
	 * Normally you should not use this method, but use
	 * {@link IJavaScriptLibrarySettings#getJQueryReference()} to prevent
	 * version conflicts.
	 * 
	 * @return the single instance of the resource reference
	 */
	public static JQueryUpdateResourceReference get() {
		return INSTANCE;
	}

	protected JQueryUpdateResourceReference() {
		super(JQueryUpdateResourceReference.class, VERSION_1);
	}
}
