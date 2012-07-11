package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;
import org.odlabs.wiquery.ui.mouse.MouseJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;

public final class ModalJavaScriptResourceReference extends WiQueryJavaScriptResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final ModalJavaScriptResourceReference INSTANCE = new ModalJavaScriptResourceReference();

	private ModalJavaScriptResourceReference() {
		super(ModalJavaScriptResourceReference.class, "jquery.fancybox-1.3.5-ow.js");
	}
	
	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				ModalStyleSheetResourceReference.get(),
				EasingJavaScriptResourceReference.get(),
				MouseJavaScriptResourceReference.get()
		};
	}

	public static ModalJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
