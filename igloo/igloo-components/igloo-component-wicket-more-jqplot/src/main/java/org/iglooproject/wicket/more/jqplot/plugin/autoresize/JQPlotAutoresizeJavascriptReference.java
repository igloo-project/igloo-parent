package org.iglooproject.wicket.more.jqplot.plugin.autoresize;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.jqplot.plugin.adddomreference.JQPlotAddDomReferenceResourceReference;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;
import nl.topicus.wqplot.components.JQPlotJavaScriptResourceReference;

/**
 * Adds a JQPlot hook so that plots are automatically re-drawn each time the window is resized.
 */
public class JQPlotAutoresizeJavascriptReference extends AbstractCoreJQueryPluginResourceReference {

	private static final long serialVersionUID = -2866004739366521046L;

	private static final JQPlotAutoresizeJavascriptReference INSTANCE = new JQPlotAutoresizeJavascriptReference();

	public static JQPlotAutoresizeJavascriptReference get() {
		return INSTANCE;
	}

	private JQPlotAutoresizeJavascriptReference() {
		super(JQPlotAutoresizeJavascriptReference.class, "jqplot.autoresize.js");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(JavaScriptHeaderItem.forReference(JQPlotJavaScriptResourceReference.get()));
		dependencies.add(JavaScriptHeaderItem.forReference(JQPlotAddDomReferenceResourceReference.get()));
		return dependencies;
	}

}
