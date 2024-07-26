package org.iglooproject.wicket.more.jqplot.config;

import nl.topicus.wqplot.options.PlotBarDirection;
import nl.topicus.wqplot.options.PlotBarRendererOptions;
import nl.topicus.wqplot.options.PlotLineRendererOptions;
import nl.topicus.wqplot.options.PlotPieRendererOptions;

/** A factory providing sensible defaults for configurations. */
public class DefaultJQPlotRendererOptionsFactory implements IJQPlotRendererOptionsFactory {

  @Override
  public PlotPieRendererOptions newPlotPieRendererOptions() {
    return new PlotPieRendererOptions()
        .setShowDataLabels(true)
        .setPadding(10.0)
        .setSliceMargin(7.0);
  }

  @Override
  public PlotBarRendererOptions newPlotBarRendererOptions() {
    return new PlotBarRendererOptions()
        .setBarDirection(PlotBarDirection.vertical)
        .setBarPadding(0.0);
  }

  @Override
  public PlotBarRendererOptions newPlotStackedBarRendererOptions() {
    return new PlotBarRendererOptions().setBarDirection(PlotBarDirection.vertical);
  }

  @Override
  public PlotLineRendererOptions newPlotLineRendererOptions() {
    return new PlotLineRendererOptions();
  }

  @Override
  public PlotLineRendererOptions newPlotStackedLineRendererOptions() {
    return new PlotLineRendererOptions();
  }
}
