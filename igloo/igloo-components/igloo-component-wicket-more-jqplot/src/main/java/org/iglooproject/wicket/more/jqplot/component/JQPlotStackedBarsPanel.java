package org.iglooproject.wicket.more.jqplot.component;

import java.util.Locale;
import java.util.Map;
import nl.topicus.wqplot.components.plugins.JQPlotBarRenderer;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;
import org.iglooproject.wicket.more.jqplot.config.AbstractJQPlotConfigurer;
import org.iglooproject.wicket.more.jqplot.data.adapter.IJQPlotDataAdapter;
import org.iglooproject.wicket.more.jqplot.data.adapter.JQPlotDataAdapters;

public class JQPlotStackedBarsPanel<S, K, V extends Number & Comparable<V>, TK>
    extends JQPlotPanel<S, K, V, TK> {

  private static final long serialVersionUID = -5575918534912813908L;

  public JQPlotStackedBarsPanel(String id, IJQPlotDataAdapter<S, K, V, TK> dataAdapter) {
    super(id, JQPlotDataAdapters.fix(dataAdapter));

    add(
        new AbstractJQPlotConfigurer<S, K>() {
          private static final long serialVersionUID = 1L;

          @Override
          public void configure(
              PlotOptions options,
              Map<? extends S, PlotSeries> seriesMap,
              Map<? extends K, PlotTick> keysMap,
              Locale locale) {
            options.setStackSeries(true);
            options
                .getSeriesDefaults()
                .setFillToZero(true)
                .setRenderer(JQPlotBarRenderer.get())
                .setRendererOptions(getOptionsFactory().newPlotStackedBarRendererOptions());
          }
        });
  }
}
