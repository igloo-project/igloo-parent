package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util;

import java.util.Collections;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.resource.JQueryPluginResourceReference;

import com.google.common.collect.Iterables;

public abstract class AbstractCoreJQueryPluginResourceReference extends JQueryPluginResourceReference {

	private static final long serialVersionUID = -1602756285653913404L;

	public AbstractCoreJQueryPluginResourceReference(Class<?> scope, String name) {
		super(scope, name);
	}

	@Override
	public Iterable<? extends HeaderItem> getDependencies() {
		return Iterables.concat(super.getDependencies(), getInternalDependencies());
	}

	protected Iterable<? extends HeaderItem> getInternalDependencies() {
		return Collections.emptyList();
	}
	
}
