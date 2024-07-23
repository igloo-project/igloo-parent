package org.iglooproject.wicket.more.jqplot.data.adapter;

import com.google.common.collect.Table;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import nl.topicus.wqplot.components.plugins.JQPlotDateAxisRenderer;
import nl.topicus.wqplot.components.plugins.JQPlotDateAxisRendererResourceReference;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.jqplot.data.provider.IJQPlotDataProvider;
import org.iglooproject.wicket.more.jqplot.data.provider.JQPlotTableDataProvider;

/**
 * Adapter for data with continuous date keys.
 *
 * <p>This is only useful if your data references keys from a continuous domain (i.e. with a high or
 * infinite number of values in any given range), such as dates precise to the second.
 *
 * <p>If your data keys are only "discrete" keys (for example the first day of each week at
 * midnight), use a {@link JQPlotDiscreteKeysDataAdapter}.
 *
 * @see JQPlotDiscreteKeyDataAdapter
 */
public class JQPlotContinuousDateKeysDataAdapter<S, K extends Date, V extends Number>
    extends AbstractJQPlotContinuousKeysDataAdapter<S, K, V> {

  private static final long serialVersionUID = 2416002233634069642L;

  /**
   * @param tableModel
   * @param formatString A format string as specified in <code>jqplot.dateAxisRenderer.js</code>
   *     (this file is located in the same package as {@link
   *     JQPlotDateAxisRendererResourceReference}).
   */
  public JQPlotContinuousDateKeysDataAdapter(
      IModel<Table<S, K, V>> tableModel, String formatString) {
    this(new JQPlotTableDataProvider<>(tableModel), formatString);
  }

  /**
   * @param dataProvider
   * @param formatString A format string as specified in <code>jqplot.dateAxisRenderer.js</code>
   *     (this file is located in the same package as {@link
   *     JQPlotDateAxisRendererResourceReference}).
   */
  public JQPlotContinuousDateKeysDataAdapter(
      IJQPlotDataProvider<S, K, V> dataProvider, String formatString) {
    super(dataProvider, formatString);
  }

  @Override
  public void configure(
      PlotOptions options,
      Map<? extends S, PlotSeries> seriesMap,
      Map<? extends K, PlotTick> keysMap,
      Locale locale) {
    options.getAxes().getXaxis().setRenderer(JQPlotDateAxisRenderer.get());
    super.configure(options, seriesMap, keysMap, locale);
  }
}
