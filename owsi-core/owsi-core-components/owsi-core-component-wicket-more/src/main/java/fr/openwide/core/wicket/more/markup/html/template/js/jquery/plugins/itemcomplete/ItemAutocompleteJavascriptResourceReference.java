package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemcomplete;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;
import org.odlabs.wiquery.ui.position.PositionJavaScriptResourceReference;
import org.odlabs.wiquery.ui.widget.WidgetJavaScriptResourceReference;

public final class ItemAutocompleteJavascriptResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = -771231799419848939L;

	private static final ItemAutocompleteJavascriptResourceReference INSTANCE = new ItemAutocompleteJavascriptResourceReference();

	private ItemAutocompleteJavascriptResourceReference() {
		super(ItemAutocompleteJavascriptResourceReference.class, "jquery.ui.itemautocomplete.js");
	}

	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				WidgetJavaScriptResourceReference.get(),
				PositionJavaScriptResourceReference.get()
		};
	}

	public static ItemAutocompleteJavascriptResourceReference get() {
		return INSTANCE;
	}

}
