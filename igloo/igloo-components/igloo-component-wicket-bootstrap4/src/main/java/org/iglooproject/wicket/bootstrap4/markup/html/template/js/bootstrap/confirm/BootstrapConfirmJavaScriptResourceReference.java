package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.confirm;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.BootstrapModalManagerJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

import com.google.common.collect.Lists;

public final class BootstrapConfirmJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final BootstrapConfirmJavaScriptResourceReference INSTANCE = new BootstrapConfirmJavaScriptResourceReference();

	private BootstrapConfirmJavaScriptResourceReference() {
		super(BootstrapConfirmJavaScriptResourceReference.class, "bootstrap-confirm.js");
	}
	
	@Override
	protected List<HeaderItem> getPluginDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayList();
		dependencies.add(JavaScriptHeaderItem.forReference(BootstrapModalManagerJavaScriptResourceReference.get()));
		return dependencies;
	}

	public static BootstrapConfirmJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
