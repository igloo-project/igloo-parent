package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap4.carousel;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap4.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapCarouselJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	
	private static final long serialVersionUID = 2483430588472990612L;
	
	private static final BootstrapCarouselJavaScriptResourceReference INSTANCE = new BootstrapCarouselJavaScriptResourceReference();
	
	private BootstrapCarouselJavaScriptResourceReference() {
		super(BootstrapCarouselJavaScriptResourceReference.class, "carousel.js");
	}
	
	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(BootstrapUtilJavaScriptResourceReference.get());
	}
	
	public static BootstrapCarouselJavaScriptResourceReference get() {
		return INSTANCE;
	}
}
