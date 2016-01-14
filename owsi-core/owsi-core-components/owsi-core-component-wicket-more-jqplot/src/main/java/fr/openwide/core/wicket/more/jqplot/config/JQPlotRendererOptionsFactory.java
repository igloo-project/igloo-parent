package fr.openwide.core.wicket.more.jqplot.config;

import nl.topicus.wqplot.options.PlotBarDirection;
import nl.topicus.wqplot.options.PlotBarRendererOptions;
import nl.topicus.wqplot.options.PlotLineRendererOptions;
import nl.topicus.wqplot.options.PlotPieRendererOptions;

/**
 * A factory providing sensible defaults for configurations.
 * 
 * You can create your very own factory and pass it to the JQPlotPanel.
 * 
 * It is supposed to be injected in the JQPlotPanel components.
 */
public class JQPlotRendererOptionsFactory {

	public PlotPieRendererOptions newPlotPieRendererOptions() {
		return new PlotPieRendererOptions()
				.setShowDataLabels(true)
				.setPadding(10.0)
				.setSliceMargin(7.0);
	}
	
	public PlotBarRendererOptions newPlotBarRendererOptions() {
		return new PlotBarRendererOptions()
				.setBarDirection(PlotBarDirection.vertical)
				.setBarPadding(0.0);
	}
	
	public PlotBarRendererOptions newPlotStackedBarRendererOptions() {
		return new PlotBarRendererOptions()
				.setBarDirection(PlotBarDirection.vertical);
	}
	
	public PlotLineRendererOptions newPlotLineRendererOptions() {
		return new PlotLineRendererOptions();
	}
	
}
