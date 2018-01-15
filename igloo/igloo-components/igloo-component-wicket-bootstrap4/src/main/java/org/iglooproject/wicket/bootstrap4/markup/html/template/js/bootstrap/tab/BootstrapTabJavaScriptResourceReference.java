package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tab;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapTabJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 4895439022427021364L;

	private static final BootstrapTabJavaScriptResourceReference INSTANCE = new BootstrapTabJavaScriptResourceReference();

	private BootstrapTabJavaScriptResourceReference() {
		super(BootstrapTabJavaScriptResourceReference.class, "tab.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(BootstrapUtilJavaScriptResourceReference.get());
	}

	public static BootstrapTabJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
