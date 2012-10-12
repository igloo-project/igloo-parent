package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemit;

import org.apache.wicket.markup.head.HeaderItem;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;
import org.odlabs.wiquery.ui.effects.BlindEffectJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemcomplete.ItemAutocompleteJavascriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.json.JsonJavascriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class ItemItJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final ItemItJavascriptResourceReference INSTANCE = new ItemItJavascriptResourceReference();

	private ItemItJavascriptResourceReference() {
		super(ItemItJavascriptResourceReference.class, "jquery.ui.itemit.js");
	}

	@Override
	public Iterable<? extends HeaderItem> getPluginDependencies() {
		return JavaScriptHeaderItems.forReferences(
				ItemAutocompleteJavascriptResourceReference.get(),
				JsonJavascriptResourceReference.get(),
				EasingJavaScriptResourceReference.get(),
				BlindEffectJavaScriptResourceReference.get());
	}

	public static ItemItJavascriptResourceReference get() {
		return INSTANCE;
	}

}
