package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.itemcomplete;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;
import org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference;

public final class ItemAutocompleteJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -771231799419848939L;

	private static final ItemAutocompleteJavascriptResourceReference INSTANCE = new ItemAutocompleteJavascriptResourceReference();

	private ItemAutocompleteJavascriptResourceReference() {
		super(ItemAutocompleteJavascriptResourceReference.class, "jquery.ui.itemautocomplete.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(
				JQueryUIJavaScriptResourceReference.get()
		);
	}

	public static ItemAutocompleteJavascriptResourceReference get() {
		return INSTANCE;
	}

}
