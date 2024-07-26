package org.iglooproject.wicket.more.jqplot.config;

import java.util.Locale;
import java.util.Map;
import nl.topicus.wqplot.components.plugins.JQPlotCanvasAxisTickRenderer;
import nl.topicus.wqplot.components.plugins.JQPlotPieRenderer;
import nl.topicus.wqplot.options.PlotCanvasAxisTickRendererOptions;
import nl.topicus.wqplot.options.PlotLegendLocation;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;
import nl.topicus.wqplot.options.PlotTooltipAxes;
import org.iglooproject.wicket.more.jqplot.util.ChartColors;
import org.springframework.beans.BeanUtils;

/** This configurer defines a default layout for the graphs. */
public final class DefaultLayoutJQPlotConfigurer extends AbstractJQPlotConfigurer<Object, Object> {

  private static final long serialVersionUID = 6956017667747223236L;

  private static final DefaultLayoutJQPlotConfigurer INSTANCE = new DefaultLayoutJQPlotConfigurer();

  public static final DefaultLayoutJQPlotConfigurer get() {
    return INSTANCE;
  }

  public DefaultLayoutJQPlotConfigurer() {}

  @Override
  public void configure(
      PlotOptions options,
      Map<? extends Object, PlotSeries> seriesMap,
      Map<? extends Object, PlotTick> keysMap,
      Locale locale) {
    // Grid
    options
        .getGrid()
        .setBackground(ChartColors.toCssString(ChartColors.TRANSPARENT))
        .setDrawBorder(true)
        .setShadow(false);

    // Highlighter
    options
        .getHighlighter()
        .setShow(true)
        .setShowMarker(false)
        .setShowTooltip(true)
        .setShowLabel(true)
        .setFormatString("%s - %s")
        .setTooltipAxes(PlotTooltipAxes.y);

    // X axis
    options.getAxes().getXaxis().setTickRenderer(JQPlotCanvasAxisTickRenderer.get().getName());
    PlotCanvasAxisTickRendererOptions tickOptions = new PlotCanvasAxisTickRendererOptions();
    BeanUtils.copyProperties(
        options.getAxes().getXaxis().getTickOptions(),
        tickOptions); // Preserve previously set tick options
    options
        .getAxes()
        .getXaxis()
        .setTickOptions(
            tickOptions
                .setFontSize("9pt")
                .setAngle(-45.0)
                .setFontWeight("normal")
                .setFontStretch(1.0));
    options.getAxes().getXaxis().getTickOptions().setShowGridline(false);

    // Y axis
    options
        .getAxes()
        .getYaxis()
        .setAutoscale(true)
        .getTickOptions()
        .setFormatString("%d")
        .setFontSize("10pt")
        .setShowMark(true)
        .setShowLabel(true);

    // Legend
    options.getLegend().setShow(true);
    if (JQPlotPieRenderer.get().getName().equals(options.getSeriesDefaults().getRenderer())) {
      options.getLegend().setLocation(PlotLegendLocation.sw);
    } else {
      options.getLegend().setLocation(PlotLegendLocation.nw);
    }
  }
}
