package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.wicket.Application;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.odlabs.wiquery.core.resources.JavaScriptHeaderItems;

public abstract class JQueryJavaScriptResourceReference extends JavaScriptResourceReference {

	private static final long serialVersionUID = -1602756285653913404L;

	public JQueryJavaScriptResourceReference(Class<?> scope, String name) {
		super(scope, name);
	}

	@Override
	public Iterable<? extends HeaderItem> getDependencies() {
		return JavaScriptHeaderItems.forReferences(getCoreAndInternalDependencies());
	}

	private ResourceReference[] getCoreAndInternalDependencies() {
		return ArrayUtils.add(getInternalDependencies(),
				Application.get().getJavaScriptLibrarySettings().getJQueryReference());
	}

	protected ResourceReference[] getInternalDependencies() {
		return new ResourceReference[] {};
	}
}
