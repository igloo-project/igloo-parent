package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util;

import java.util.Collections;
import java.util.List;

import org.apache.http.util.Args;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.markup.html.template.js.owsi.OwsiUtilsJavaScriptResourceReference;

public abstract class AbstractCoreJQueryPluginResourceReference extends JQueryPluginResourceReference {

	private static final long serialVersionUID = -1602756285653913404L;

	public AbstractCoreJQueryPluginResourceReference(Class<?> scope, String name) {
		super(scope, name);
	}

	@Override
	public final List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(JavaScriptHeaderItem.forReference(OwsiUtilsJavaScriptResourceReference.get()));
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
