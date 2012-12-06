package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util;

import java.util.Collections;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.resource.JQueryPluginResourceReference;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.markup.html.template.js.owsi.OwsiUtilsJavaScriptResourceReference;

public abstract class AbstractCoreJQueryPluginResourceReference extends JQueryPluginResourceReference {

	private static final long serialVersionUID = -1602756285653913404L;

	public AbstractCoreJQueryPluginResourceReference(Class<?> scope, String name) {
		super(scope, name);
	}

	@Override
	public final Iterable<? extends HeaderItem> getDependencies() {
		return Iterables.concat(super.getDependencies(),
				Lists.newArrayList(JavaScriptHeaderItem.forReference(OwsiUtilsJavaScriptResourceReference.get())),
				getPluginDependencies());
	}

	protected Iterable<? extends HeaderItem> getPluginDependencies() {
		return Collections.emptyList();
	}
	
}
