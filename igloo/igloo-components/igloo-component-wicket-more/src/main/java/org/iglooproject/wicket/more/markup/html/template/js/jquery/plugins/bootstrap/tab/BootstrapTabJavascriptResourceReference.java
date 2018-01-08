package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tab;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapTabJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 4895439022427021364L;

	private static final BootstrapTabJavascriptResourceReference INSTANCE = new BootstrapTabJavascriptResourceReference();

	private BootstrapTabJavascriptResourceReference() {
		super(BootstrapTabJavascriptResourceReference.class, "tab.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(BootstrapUtilJavaScriptResourceReference.get());
	}

	public static BootstrapTabJavascriptResourceReference get() {
		return INSTANCE;
	}

}
