package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemit;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;
import org.odlabs.wiquery.ui.effects.BlindEffectJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemcomplete.ItemAutocompleteJavascriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.json.JsonJavascriptResourceReference;

public final class ItemItJavascriptResourceReference extends WiQueryJavaScriptResourceReference {

	private static final long serialVersionUID = 1302122786281225341L;

	private static final ItemItJavascriptResourceReference INSTANCE = new ItemItJavascriptResourceReference();

	private ItemItJavascriptResourceReference() {
		super(ItemItJavascriptResourceReference.class, "jquery.ui.itemit.js");
	}

	@Override
	public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
		return new AbstractResourceDependentResourceReference[] {
				ItemAutocompleteJavascriptResourceReference.get(),
				JsonJavascriptResourceReference.get(),
				EasingJavaScriptResourceReference.get(),
				BlindEffectJavaScriptResourceReference.get()
		};
	}

	public static ItemItJavascriptResourceReference get() {
		return INSTANCE;
	}

}
