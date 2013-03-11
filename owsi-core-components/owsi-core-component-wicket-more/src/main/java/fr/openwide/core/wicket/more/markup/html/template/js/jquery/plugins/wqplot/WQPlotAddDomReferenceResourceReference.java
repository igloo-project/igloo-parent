package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.wqplot;

import java.util.List;

import nl.topicus.wqplot.components.JQPlotJavaScriptResourceReference;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.AbstractCoreJQueryPluginResourceReference;

/**
 * Charge le script activant la possibilité de faire référence aux diagrammes JQPlot en javascript,
 * pour les re-dessiner par exemple ('replot').
 */
public class WQPlotAddDomReferenceResourceReference extends AbstractCoreJQueryPluginResourceReference {
	private static final long serialVersionUID = -8799742276479282371L;
	
	private static final WQPlotAddDomReferenceResourceReference INSTANCE = new WQPlotAddDomReferenceResourceReference();

	private WQPlotAddDomReferenceResourceReference() {
		super(WQPlotAddDomReferenceResourceReference.class, "wqplot-addDomReference.js");
	}
	
	@Override
	protected Iterable<? extends HeaderItem> getPluginDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayList();
		dependencies.add(JavaScriptHeaderItem.forReference(JQPlotJavaScriptResourceReference.get()));
		return dependencies;
	}

	public static WQPlotAddDomReferenceResourceReference get() {
		return INSTANCE;
	}

}
