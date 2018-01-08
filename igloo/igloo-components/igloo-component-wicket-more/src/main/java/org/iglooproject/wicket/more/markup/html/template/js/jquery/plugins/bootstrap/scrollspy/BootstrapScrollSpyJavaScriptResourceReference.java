package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.scrollspy;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapScrollSpyJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1777703340106781128L;
	
	private static final BootstrapScrollSpyJavaScriptResourceReference INSTANCE = new BootstrapScrollSpyJavaScriptResourceReference();

	private BootstrapScrollSpyJavaScriptResourceReference() {
		super(BootstrapScrollSpyJavaScriptResourceReference.class, "scrollspy.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(BootstrapUtilJavaScriptResourceReference.get());
	}

	public static BootstrapScrollSpyJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
