package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.markup.head.HeaderItem;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public class FancyboxJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;

	private static final FancyboxJavaScriptResourceReference INSTANCE = new FancyboxJavaScriptResourceReference();

	private FancyboxJavaScriptResourceReference() {
		super(FancyboxJavaScriptResourceReference.class, "jquery.fancybox-1.3.4/fancybox/jquery.fancybox-1.3.4.js");
	}
	
	@Override
	public Iterable<? extends HeaderItem> getPluginDependencies() {
		return JavaScriptHeaderItems.forReferences(EasingJavaScriptResourceReference.get());
	}

	public static FancyboxJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
