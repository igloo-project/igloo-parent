package org.iglooproject.wicket.more.jqplot.config;

import nl.topicus.wqplot.options.PlotBarRendererOptions;
import nl.topicus.wqplot.options.PlotLineRendererOptions;
import nl.topicus.wqplot.options.PlotPieRendererOptions;
import org.iglooproject.wicket.more.jqplot.component.JQPlotPanel;

/**
 * A factory providing default options for various {@link JQPlotPanel}s.
 *
 * <p>The factory is supposed to be injected in the JQPlotPanel components, by defining it as a
 * Spring bean in {@link JQPlotJavaConfig#jqPlotRendererOptionsFactory()}
 */
public interface IJQPlotRendererOptionsFactory {

  PlotPieRendererOptions newPlotPieRendererOptions();

  PlotBarRendererOptions newPlotBarRendererOptions();

  PlotBarRendererOptions newPlotStackedBarRendererOptions();

  PlotLineRendererOptions newPlotLineRendererOptions();

  PlotLineRendererOptions newPlotStackedLineRendererOptions();
}
