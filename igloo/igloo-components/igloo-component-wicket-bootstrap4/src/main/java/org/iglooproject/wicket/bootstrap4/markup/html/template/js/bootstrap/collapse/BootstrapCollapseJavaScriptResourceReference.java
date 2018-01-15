package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.collapse;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;


public final class BootstrapCollapseJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -5388425553542523759L;

	private static final BootstrapCollapseJavaScriptResourceReference INSTANCE = new BootstrapCollapseJavaScriptResourceReference();

	private BootstrapCollapseJavaScriptResourceReference() {
		super(BootstrapCollapseJavaScriptResourceReference.class, "collapse.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(BootstrapUtilJavaScriptResourceReference.get());
	}

	public static BootstrapCollapseJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
