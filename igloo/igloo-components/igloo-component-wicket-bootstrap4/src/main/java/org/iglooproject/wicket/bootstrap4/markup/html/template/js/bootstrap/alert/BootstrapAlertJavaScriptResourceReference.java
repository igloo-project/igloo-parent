package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.alert;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class BootstrapAlertJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;
	
	private static final BootstrapAlertJavaScriptResourceReference INSTANCE = new BootstrapAlertJavaScriptResourceReference();

	private BootstrapAlertJavaScriptResourceReference() {
		super(BootstrapAlertJavaScriptResourceReference.class, "alert.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(BootstrapUtilJavaScriptResourceReference.get());
	}

	public static BootstrapAlertJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
