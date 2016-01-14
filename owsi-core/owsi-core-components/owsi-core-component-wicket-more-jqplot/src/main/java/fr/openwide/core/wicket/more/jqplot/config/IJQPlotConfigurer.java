package fr.openwide.core.wicket.more.jqplot.config;

import java.util.Locale;
import java.util.Map;

import org.apache.wicket.model.IDetachable;

import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;

public interface IJQPlotConfigurer<S, K> extends IDetachable {
	
	void initialize(PlotOptions options);
	
	void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap,
			Map<? extends K, PlotTick> keysMap, Locale locale);

}
