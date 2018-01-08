package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.button;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapButtonJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	
	private static final long serialVersionUID = 2483430588472990612L;
	
	private static final BootstrapButtonJavaScriptResourceReference INSTANCE = new BootstrapButtonJavaScriptResourceReference();
	
	private BootstrapButtonJavaScriptResourceReference() {
		super(BootstrapButtonJavaScriptResourceReference.class, "button.js");
	}
	
	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(BootstrapUtilJavaScriptResourceReference.get());
	}
	
	public static BootstrapButtonJavaScriptResourceReference get() {
		return INSTANCE;
	}
}
