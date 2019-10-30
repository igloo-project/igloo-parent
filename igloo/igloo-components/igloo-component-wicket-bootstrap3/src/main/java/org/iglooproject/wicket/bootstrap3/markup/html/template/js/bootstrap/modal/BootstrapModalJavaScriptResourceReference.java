package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.modal;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapModalJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final BootstrapModalJavaScriptResourceReference INSTANCE = new BootstrapModalJavaScriptResourceReference();

	private BootstrapModalJavaScriptResourceReference() {
		super(BootstrapModalJavaScriptResourceReference.class, "bootstrap-modal.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(JavaScriptHeaderItem.forReference(BootstrapModalManagerJavaScriptResourceReference.get()));
		return dependencies;
	}

	public static BootstrapModalJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
