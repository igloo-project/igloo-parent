package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;
import org.odlabs.wiquery.ui.mouse.MouseJavaScriptResourceReference;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

public final class ModalJavaScriptResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final ModalJavaScriptResourceReference INSTANCE = new ModalJavaScriptResourceReference();

	private ModalJavaScriptResourceReference() {
		super(ModalJavaScriptResourceReference.class, "jquery.fancybox-1.3.5-ow.js");
	}
	
	@Override
	protected Iterable<? extends HeaderItem> getPluginDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayList();
		Iterables.addAll(dependencies, JavaScriptHeaderItems.forReferences(
				EasingJavaScriptResourceReference.get(),
				MouseJavaScriptResourceReference.get()
		));
		dependencies.add(CssHeaderItem.forReference(ModalStyleSheetResourceReference.get()));
		return dependencies;
	}

	public static ModalJavaScriptResourceReference get() {
		return INSTANCE;
	}

}
