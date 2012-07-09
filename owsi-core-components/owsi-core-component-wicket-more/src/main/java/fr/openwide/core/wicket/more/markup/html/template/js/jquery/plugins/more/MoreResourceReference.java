package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.more;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public final class MoreResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = -7241670799446070904L;

	private static final MoreResourceReference INSTANCE = new MoreResourceReference();

	public MoreResourceReference() {
		super(MoreResourceReference.class, "jquery.more.js");
	}

	public static MoreResourceReference get() {
		return INSTANCE;
	}

}
