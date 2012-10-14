package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemcomplete;

import org.apache.wicket.markup.head.HeaderItem;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;
import org.odlabs.wiquery.ui.position.PositionJavaScriptResourceReference;
import org.odlabs.wiquery.ui.widget.WidgetJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class ItemAutocompleteJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -771231799419848939L;

	private static final ItemAutocompleteJavascriptResourceReference INSTANCE = new ItemAutocompleteJavascriptResourceReference();

	private ItemAutocompleteJavascriptResourceReference() {
		super(ItemAutocompleteJavascriptResourceReference.class, "jquery.ui.itemautocomplete.js");
	}

	@Override
	public Iterable<? extends HeaderItem> getPluginDependencies() {
		return JavaScriptHeaderItems.forReferences(
				WidgetJavaScriptResourceReference.get(),
				PositionJavaScriptResourceReference.get()
		);
	}

	public static ItemAutocompleteJavascriptResourceReference get() {
		return INSTANCE;
	}

}
