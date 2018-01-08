package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.dropdown;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.popper.PopperJavaScriptResourceReference;


public final class BootstrapDropDownJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -5388425553542523759L;

	private static final BootstrapDropDownJavaScriptResourceReference INSTANCE = new BootstrapDropDownJavaScriptResourceReference();

	private BootstrapDropDownJavaScriptResourceReference() {
		super(BootstrapDropDownJavaScriptResourceReference.class, "dropdown.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(
				PopperJavaScriptResourceReference.get(),
				BootstrapUtilJavaScriptResourceReference.get()
		);
	}

	public static BootstrapDropDownJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
