package org.iglooproject.wicket.more.jqplot.config;

import java.util.Locale;
import java.util.Map;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;
import org.apache.wicket.model.IDetachable;
import org.iglooproject.wicket.more.jqplot.component.JQPlotPanel;

/**
 * An interface for objects that customize a {@link JQPlotPanel}'s {@link PlotOptions options}.
 *
 * @see JQPlotPanel
 * @see JQPlotPanel#add(IJQPlotConfigurer, IJQPlotConfigurer...)
 * @see PlotOptions
 * @param <S> The type for series on the configured {@link JQPlotPanel}
 * @param <K> The type for keys (generally the X axis) on the configured {@link JQPlotPanel}
 */
public interface IJQPlotConfigurer<S, K> extends IDetachable {

  /**
   * Configure options.
   *
   * <p>This method is called each time a panel this configurer has been added to is about to be
   * rendered.
   */
  void configure(
      PlotOptions options,
      Map<? extends S, PlotSeries> seriesMap,
      Map<? extends K, PlotTick> keysMap,
      Locale locale);
}
