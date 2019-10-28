package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.resource.JQueryPluginResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.igloo.IglooUtilsJavaScriptResourceReference;

public abstract class AbstractCoreJQueryPluginResourceReference extends JQueryPluginResourceReference {

	private static final long serialVersionUID = -1602756285653913404L;

	public AbstractCoreJQueryPluginResourceReference(Class<?> scope, String name) {
		super(scope, name);
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(JavaScriptHeaderItem.forReference(IglooUtilsJavaScriptResourceReference.get()));
		return dependencies;
	}

}
