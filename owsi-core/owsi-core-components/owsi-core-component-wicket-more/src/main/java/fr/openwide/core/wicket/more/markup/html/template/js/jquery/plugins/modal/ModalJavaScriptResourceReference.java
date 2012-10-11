package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal;

import org.apache.wicket.request.resource.ResourceReference;
import org.odlabs.wiquery.ui.mouse.MouseJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractJQueryPluginResourceReference;

public final class ModalJavaScriptResourceReference extends AbstractJQueryPluginResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final ModalJavaScriptResourceReference INSTANCE = new ModalJavaScriptResourceReference();

	private ModalJavaScriptResourceReference() {
		super(ModalJavaScriptResourceReference.class, "jquery.fancybox-1.3.5-ow.js");
	}
	
	@Override
	public ResourceReference[] getInternalDependencies() {
		return new ResourceReference[] {
				ModalStyleSheetResourceReference.get(),
				EasingJavaScriptResourceReference.get(),
				MouseJavaScriptResourceReference.get()
		};
	}

	public static ModalJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
