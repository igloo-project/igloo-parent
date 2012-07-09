package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;

public final class ScrollToTopJavaScriptResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = 4300908585408220863L;

	private static final ScrollToTopJavaScriptResourceReference INSTANCE = new ScrollToTopJavaScriptResourceReference();

	private ScrollToTopJavaScriptResourceReference() {
		super(ScrollToTopJavaScriptResourceReference.class, "jquery.scrollToTop.js");
	}

	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				EasingJavaScriptResourceReference.get()
		};
	}

	public static ScrollToTopJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
