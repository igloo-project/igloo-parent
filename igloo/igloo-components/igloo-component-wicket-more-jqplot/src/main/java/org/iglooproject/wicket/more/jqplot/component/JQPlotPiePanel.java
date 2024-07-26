package org.iglooproject.wicket.more.jqplot.component;

import igloo.wicket.renderer.Renderer;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import nl.topicus.wqplot.components.plugins.JQPlotPieRenderer;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.jqplot.config.AbstractJQPlotConfigurer;
import org.iglooproject.wicket.more.jqplot.config.JQPlotConfigurers;
import org.iglooproject.wicket.more.jqplot.data.adapter.JQPlotDataAdapters;
import org.iglooproject.wicket.more.jqplot.data.adapter.JQPlotPieChartDataAdapter;
import org.iglooproject.wicket.more.jqplot.data.provider.IJQPlotDataProvider;
import org.iglooproject.wicket.more.jqplot.data.provider.JQPlotMapDataProvider;

public class JQPlotPiePanel<K, V extends Number & Comparable<V>>
    extends JQPlotPanel<Object, K, V, String> {

  private static final long serialVersionUID = -5575918534912813908L;

  public JQPlotPiePanel(String id, IModel<Map<K, V>> mapModel, Renderer<? super K> keyRenderer) {
    this(id, new JQPlotMapDataProvider<>(mapModel), null, keyRenderer);
  }

  public JQPlotPiePanel(
      String id,
      IModel<Map<K, V>> mapModel,
      IModel<? extends Collection<? extends K>> keysModel,
      Renderer<? super K> keyRenderer) {
    this(id, new JQPlotMapDataProvider<>(mapModel), keysModel, keyRenderer);
  }

  public JQPlotPiePanel(
      String id, IJQPlotDataProvider<Object, K, V> dataProvider, Renderer<? super K> keyRenderer) {
    this(id, dataProvider, null, keyRenderer);
  }

  public JQPlotPiePanel(
      String id,
      IJQPlotDataProvider<Object, K, V> dataProvider,
      IModel<? extends Collection<? extends K>> keysModel,
      Renderer<? super K> keyRenderer) {
    this(id, new JQPlotPieChartDataAdapter<>(dataProvider, null, keysModel, keyRenderer));
  }

  public JQPlotPiePanel(String id, JQPlotPieChartDataAdapter<Object, K, V> dataAdapter) {
    super(id, JQPlotDataAdapters.fix(dataAdapter));

    add(
        new AbstractJQPlotConfigurer<Object, K>() {
          private static final long serialVersionUID = 1L;

          @Override
          public void configure(
              PlotOptions options,
              Map<? extends Object, PlotSeries> seriesMap,
              Map<? extends K, PlotTick> keysMap,
              Locale locale) {
            options
                .getSeriesDefaults()
                .setRenderer(JQPlotPieRenderer.get())
                .setRendererOptions(getOptionsFactory().newPlotPieRendererOptions());

            // jqPlot bug workaround
            // The PieCharts don't have any axis, and it doesn't work if this option is set to true.
            options.getHighlighter().setUseAxesFormatters(false);
          }
        });
    add(JQPlotConfigurers.xAxisDisabled());
  }
}
