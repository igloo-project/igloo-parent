package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;
import org.apache.wicket.util.lang.Args;

import com.google.common.collect.Lists;

import org.iglooproject.wicket.more.markup.html.template.js.igloo.IglooUtilsJavaScriptResourceReference;

public abstract class AbstractCoreJQueryPluginResourceReference extends JQueryPluginResourceReference {

	private static final long serialVersionUID = -1602756285653913404L;

	public AbstractCoreJQueryPluginResourceReference(Class<?> scope, String name) {
		super(scope, name);
	}

	@Override
	public final List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(JavaScriptHeaderItem.forReference(IglooUtilsJavaScriptResourceReference.get()));
		dependencies.addAll(getPluginDependencies());
		return dependencies;
	}

	protected List<HeaderItem> getPluginDependencies() {
		return Collections.emptyList();
	}
	
	protected List<HeaderItem> forReferences(ResourceReference... references) {
		Args.notNull(references, "references");
		List<HeaderItem> dependencies = Lists.newArrayListWithExpectedSize(references.length);
		for (ResourceReference reference : references) {
			dependencies.add(JavaScriptHeaderItem.forReference(reference));
		}
		return dependencies;
	}
}
