package fr.openwide.core.wicket.more.jqplot.plugin.adddomreference;

import java.util.List;

import nl.topicus.wqplot.components.JQPlotJavaScriptResourceReference;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

/**
 * Sets a JQPlot configuration option so that plots can be referenced in Javascript.
 * <p>This is particularly useful when we have to manually trigger a plot re-draw (using the 'replot' method).
 */
public final class JQPlotAddDomReferenceResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final JQPlotAddDomReferenceResourceReference INSTANCE = new JQPlotAddDomReferenceResourceReference();

	private JQPlotAddDomReferenceResourceReference() {
		super(JQPlotAddDomReferenceResourceReference.class, "jqplot-addDomReference.js");
	}
	
	@Override
	protected List<HeaderItem> getPluginDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayList();
		dependencies.add(JavaScriptHeaderItem.forReference(JQPlotJavaScriptResourceReference.get()));
		return dependencies;
	}

	public static JQPlotAddDomReferenceResourceReference get() {
		return INSTANCE;
	}

}
