package fr.openwide.core.wicket.more.jqplot.config;

import java.util.Locale;
import java.util.Map;

import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;

/**
 * Abstract base class for inline implementations of {@link IJQPlotConfigurer}
 */
public abstract class AbstractJQPlotConfigurer<S, K> implements IJQPlotConfigurer<S, K> {

	private static final long serialVersionUID = 4674594835911474833L;

	@Override
	public void initialize(PlotOptions options) {
		// Does nothing.
		// To be overriden by subclasses.
	}

	@Override
	public void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap,
			Map<? extends K, PlotTick> keysMap, Locale locale) {
		// Does nothing.
		// To be overriden by subclasses.
	}
	
	@Override
	public void detach() {
		// Does nothing.
		// To be overriden by subclasses.
	}

}
