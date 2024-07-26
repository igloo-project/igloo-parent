package org.iglooproject.wicket.more.jqplot.data.adapter;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import nl.topicus.wqplot.data.StringNumberSeries;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;
import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.wicket.more.jqplot.data.provider.IJQPlotDataProvider;

/**
 * Adapter for the pie charts data.
 *
 * <p>Features:
 *
 * <ul>
 *   <li>displays automatically all the series, even the ones without data
 *   <li>integrates the value in the form [[['label',value],['label2',value3]]], as it's the only
 *       way to associate correct labels to the pie charts data.
 * </ul>
 */
public class JQPlotPieChartDataAdapter<S, K, V extends Number & Comparable<V>>
    extends AbstractMissingValuesJQPlotDataAdapter<S, K, V, String> {

  private static final long serialVersionUID = 3961697302069579609L;

  private final IRenderer<? super K> keyRenderer;

  public JQPlotPieChartDataAdapter(
      IJQPlotDataProvider<S, K, V> dataProvider,
      IModel<? extends Collection<? extends S>> seriesModel,
      IModel<? extends Collection<? extends K>> keysModel,
      IRenderer<? super K> keyRenderer) {
    this(dataProvider, seriesModel, keysModel, null, keyRenderer);
  }

  public JQPlotPieChartDataAdapter(
      IJQPlotDataProvider<S, K, V> dataProvider,
      IModel<? extends Collection<? extends S>> seriesModel,
      IModel<? extends Collection<? extends K>> keysModel,
      IModel<? extends V> missingValueReplacementModel,
      IRenderer<? super K> keyRenderer) {
    super(dataProvider, seriesModel, keysModel, missingValueReplacementModel);
    this.keyRenderer = keyRenderer;
  }

  @Override
  public Collection<StringNumberSeries<V>> getObject(Locale locale) {
    Iterable<? extends K> keysTicks = getKeysTicks();

    Collection<StringNumberSeries<V>> result = Lists.newArrayList();
    for (S series : getSeriesTicks()) {
      StringNumberSeries<V> seriesData = createSeriesData(locale, series, keysTicks);
      result.add(seriesData);
    }
    return result;
  }

  private StringNumberSeries<V> createSeriesData(
      Locale locale, S series, Iterable<? extends K> keys) {
    StringNumberSeries<V> seriesData = new StringNumberSeries<>();
    for (K key : keys) {
      V value = getValue(series, key);
      seriesData.addEntry(keyRenderer.render(key, locale), value);
    }
    return seriesData;
  }

  @Override
  public void configure(
      PlotOptions options,
      Map<? extends S, PlotSeries> seriesMap,
      Map<? extends K, PlotTick> keysMap,
      Locale locale) {
    // Is only so that we don't serialize the keys.
    for (Map.Entry<? extends K, PlotTick> entry : keysMap.entrySet()) {
      K key = entry.getKey();
      PlotTick tick = entry.getValue();
      tick.setVal(keyRenderer.render(key, locale));
    }
  }
}
