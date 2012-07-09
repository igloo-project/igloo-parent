package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrollinviewport;

import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

public final class ScrollInViewportResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = -1012203655427832428L;

	private static final ScrollInViewportResourceReference INSTANCE = new ScrollInViewportResourceReference();

	public ScrollInViewportResourceReference() {
		super(ScrollInViewportResourceReference.class, "jquery.scrollInViewport.js");
	}

	public static ScrollInViewportResourceReference get() {
		return INSTANCE;
	}
}
