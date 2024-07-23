package org.iglooproject.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import nl.topicus.wqplot.data.AbstractSeries;
import nl.topicus.wqplot.data.SeriesEntry;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.jqplot.config.IJQPlotConfigurer;
import org.iglooproject.wicket.more.jqplot.data.provider.IJQPlotDataProvider;

/**
 * An interface for objects that provide access to data in JQPlot format.
 *
 * <p>The access to these data is provided through {@link IModel#getObject()} (since this interface
 * extends {@link IModel}).
 *
 * <p>Generally, the raw data are internally extracted from a {@link IJQPlotDataProvider}.
 *
 * @see IJQPlotDataProvider
 * @param <S> The type for series
 * @param <K> The type for keys (generally the X axis)
 * @param <V> The type for values (generally the Y axis)
 */
public interface IJQPlotDataAdapter<S, K, V, TK>
    extends IJQPlotDataProvider<S, K, V>,
        IJQPlotConfigurer<S, K>,
        IComponentAssignedModel<
            Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>>> {

  /**
   * Finalize options.
   *
   * <p>This method is called after all configurers have had a chance to change the plot options.
   *
   * <p>It is guaranteed that no change will be performed on the options between when this method is
   * called and the panel's rendering.
   */
  void afterConfigure(
      PlotOptions options,
      Map<? extends S, PlotSeries> seriesMap,
      Map<? extends K, PlotTick> keysMap,
      Locale locale);

  /**
   * @return The series that should appear in the plot. There may be more elements than in {@link
   *     #getSeries()}, since some series that don't have corresponding values may be required to
   *     appear in the plot anyway (in the legend, for instance).
   */
  Collection<S> getSeriesTicks();

  /**
   * @return The keys that should appear in the plot. There may be more elements than in {@link
   *     #getKeys()}, since some keys that don't have corresponding values may be required to appear
   *     in the plot anyway (as ticks on the X axis, for instance).
   */
  Collection<K> getKeysTicks();
}
