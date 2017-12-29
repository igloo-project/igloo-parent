package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.itemit;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.wicketstuff.wiquery.ui.effects.BlindEffectJavaScriptResourceReference;

import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.itemcomplete.ItemAutocompleteJavascriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.json.JsonJavascriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class ItemItJavascriptResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final ItemItJavascriptResourceReference INSTANCE = new ItemItJavascriptResourceReference();

	private ItemItJavascriptResourceReference() {
		super(ItemItJavascriptResourceReference.class, "jquery.ui.itemit.js");
	}

	@Override
	protected List<HeaderItem> getPluginDependencies() {
		return forReferences(
				ItemAutocompleteJavascriptResourceReference.get(),
				JsonJavascriptResourceReference.get(),
				EasingJavaScriptResourceReference.get(),
				BlindEffectJavaScriptResourceReference.get()
		);
	}

	public static ItemItJavascriptResourceReference get() {
		return INSTANCE;
	}

}
