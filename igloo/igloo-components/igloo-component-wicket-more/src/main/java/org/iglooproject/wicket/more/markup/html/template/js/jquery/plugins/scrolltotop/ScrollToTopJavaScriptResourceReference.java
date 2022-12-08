package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;

public final class ScrollToTopJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 4300908585408220863L;

	private static final ScrollToTopJavaScriptResourceReference INSTANCE = new ScrollToTopJavaScriptResourceReference();

	private ScrollToTopJavaScriptResourceReference() {
		super(ScrollToTopJavaScriptResourceReference.class, "jquery.scrollToTop.js");
	}

	public static ScrollToTopJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
