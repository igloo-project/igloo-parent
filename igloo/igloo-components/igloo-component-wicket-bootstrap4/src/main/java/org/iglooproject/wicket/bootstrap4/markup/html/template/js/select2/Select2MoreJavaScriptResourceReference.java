package org.iglooproject.wicket.bootstrap4.markup.html.template.js.select2;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.util.BootstrapUtilJavaScriptResourceReference;
import org.iglooproject.wicket.more.webjars.WebjarUtil;

public class Select2MoreJavaScriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = -7035932997722320946L;

	private static final SerializableSupplier2<List<HeaderItem>> DEPENDENCIES = WebjarUtil.memoizeHeaderItemsforReferences(
		BootstrapUtilJavaScriptResourceReference.get()
	);

	private static final Select2MoreJavaScriptResourceReference INSTANCE = new Select2MoreJavaScriptResourceReference();

	private Select2MoreJavaScriptResourceReference() {
		super(Select2MoreJavaScriptResourceReference.class, "select2-more.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.addAll(DEPENDENCIES.get());
		return dependencies;
	}

	public static Select2MoreJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
