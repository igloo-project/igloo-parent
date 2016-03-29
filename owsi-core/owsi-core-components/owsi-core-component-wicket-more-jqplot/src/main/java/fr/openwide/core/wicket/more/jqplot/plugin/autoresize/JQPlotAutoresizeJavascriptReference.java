package fr.openwide.core.wicket.more.jqplot.plugin.autoresize;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;
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
	protected List<HeaderItem> getPluginDependencies() {
		return ImmutableList.<HeaderItem>of(JavaScriptHeaderItem.forReference(JQPlotJavaScriptResourceReference.get())
		);
	}

}
