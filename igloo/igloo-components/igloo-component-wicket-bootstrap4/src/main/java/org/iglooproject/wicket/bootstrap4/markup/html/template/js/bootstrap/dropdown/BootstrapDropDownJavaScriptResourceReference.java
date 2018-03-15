package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.dropdown;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.popper.PopperJavaScriptResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class BootstrapDropDownJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

	private static final long serialVersionUID = -1442288640907214154L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
			PopperJavaScriptResourceReference.get(),
			BootstrapUtilJavaScriptResourceReference.get()
	);
	
	private static final BootstrapDropDownJavaScriptResourceReference INSTANCE = new BootstrapDropDownJavaScriptResourceReference();

	private BootstrapDropDownJavaScriptResourceReference() {
		super("bootstrap/current/js/dist/dropdown.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return DEPENDENCIES.get();
	}

	public static BootstrapDropDownJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
