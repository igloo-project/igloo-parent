package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;

public class FancyboxJavaScriptResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final FancyboxJavaScriptResourceReference INSTANCE = new FancyboxJavaScriptResourceReference();

	public FancyboxJavaScriptResourceReference() {
		super(FancyboxJavaScriptResourceReference.class, "jquery.fancybox-1.3.4/fancybox/jquery.fancybox-1.3.4.js");
	}
	
	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				EasingJavaScriptResourceReference.get()
		};
	}
	
	public static FancyboxJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
