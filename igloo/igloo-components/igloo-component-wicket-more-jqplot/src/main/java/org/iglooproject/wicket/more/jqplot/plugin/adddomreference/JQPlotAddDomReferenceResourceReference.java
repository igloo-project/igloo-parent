package org.iglooproject.wicket.more.jqplot.plugin.adddomreference;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;
import nl.topicus.wqplot.components.JQPlotJavaScriptResourceReference;

/**
 * Sets a JQPlot configuration option so that plots can be referenced in Javascript.
 * <p>This is necessary for most javascript code manipulating JQPlot's plots.
 */
public final class JQPlotAddDomReferenceResourceReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -8799742276479282371L;

	private static final JQPlotAddDomReferenceResourceReference INSTANCE = new JQPlotAddDomReferenceResourceReference();

	private JQPlotAddDomReferenceResourceReference() {
		super(JQPlotAddDomReferenceResourceReference.class, "jqplot-addDomReference.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(JavaScriptHeaderItem.forReference(JQPlotJavaScriptResourceReference.get()));
		return dependencies;
	}

	public static JQPlotAddDomReferenceResourceReference get() {
		return INSTANCE;
	}

}
