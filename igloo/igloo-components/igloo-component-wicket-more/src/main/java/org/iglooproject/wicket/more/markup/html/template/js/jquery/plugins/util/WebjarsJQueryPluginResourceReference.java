package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.util;

import java.util.Locale;

import org.apache.wicket.resource.JQueryPluginResourceReference;

import de.agilecoders.wicket.webjars.request.resource.IWebjarsResourceReference;
import de.agilecoders.wicket.webjars.util.WebjarsVersion;

public class WebjarsJQueryPluginResourceReference extends JQueryPluginResourceReference
		implements IWebjarsResourceReference {

	private static final long serialVersionUID = 919512467032103146L;

	private final String originalName;

	public WebjarsJQueryPluginResourceReference(final String name) {
		super(WebjarsJQueryPluginResourceReference.class, WebjarsVersion.useRecent(name));
		this.originalName = name;
	}

	@Override
	public final String getOriginalName() {
		return originalName;
	}

	@Override
	public final Locale getLocale() {
		return null;
	}

	@Override
	public final String getStyle() {
		return null;
	}

	@Override
	public final String getVariation() {
		return null;
	}

	@Override
	public String toString() {
		return "[webjars js resource] " + getOriginalName() + " (resolved name: " + getName() + ")";
	}
}
