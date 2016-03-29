package fr.openwide.core.wicket.more.jqplot.plugin.adddomreference;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;
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
	protected List<HeaderItem> getPluginDependencies() {
		return ImmutableList.<HeaderItem>of(
				JavaScriptHeaderItem.forReference(JQPlotJavaScriptResourceReference.get())
		);
	}

	public static JQPlotAddDomReferenceResourceReference get() {
		return INSTANCE;
	}

}
