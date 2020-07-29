package org.iglooproject.wicket.more.jqplot.config;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Localizer;
import org.apache.wicket.util.convert.IConverter;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.more.jqplot.util.ChartColors;

import com.google.common.collect.Lists;

import nl.topicus.wqplot.options.PlotAxisRendererOptions;
import nl.topicus.wqplot.options.PlotBarRendererOptions;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotSeriesRendererOptions;
import nl.topicus.wqplot.options.PlotTick;

public final class JQPlotConfigurers {
	
	private JQPlotConfigurers() { }

	public static IJQPlotConfigurer<Object, Object> title(String titleKey) {
		return new TitleJQPlotConfigurer(titleKey);
	}
	private static class TitleJQPlotConfigurer extends AbstractJQPlotConfigurer<Object, Object> {
		private static final long serialVersionUID = 1L;
		
		private final String titleKey;

		public TitleJQPlotConfigurer(String titleKey) {
			super();
			this.titleKey = titleKey;
		}
		
		@Override
		public void configure(PlotOptions options, Map<?, PlotSeries> seriesMap, Map<?, PlotTick> keysMap, Locale locale) {
			options.setTitle(Localizer.get().getString(titleKey, null, null, locale, null, (String) null));
		}
	}
	
	public static <K> IJQPlotConfigurer<Object, K> keysColors(SerializableFunction2<? super K, ? extends Color> seriesColorFunction) {
		return keysColors(seriesColorFunction, seriesColorFunction);
	}
	
	public static <K> IJQPlotConfigurer<Object, K> keysColors(SerializableFunction2<? super K, ? extends Color> seriesColorFunction, SerializableFunction2<? super K, ? extends Color> negativeSeriesColorFunction) {
		return new KeysColorsFunctionJQPlotConfigurer<>(seriesColorFunction, negativeSeriesColorFunction);
	}
	private static class KeysColorsFunctionJQPlotConfigurer<K> extends AbstractJQPlotConfigurer<Object, K> {
		private static final long serialVersionUID = -7455227724829756556L;
		
		private final SerializableFunction2<? super K, ? extends Color> seriesColorFunction;
		
		private final SerializableFunction2<? super K, ? extends Color> negativeSeriesColorFunction;
		
		public KeysColorsFunctionJQPlotConfigurer(SerializableFunction2<? super K, ? extends Color> keysColorFunction,
				SerializableFunction2<? super K, ? extends Color> negativeSeriesColorFunction) {
			super();
			this.seriesColorFunction = keysColorFunction;
			this.negativeSeriesColorFunction = negativeSeriesColorFunction;
		}
		
		@Override
		public void configure(PlotOptions options, Map<?, PlotSeries> seriesMap, Map<? extends K, PlotTick> keysMap, Locale locale) {
			PlotSeriesRendererOptions rendererOptions = options.getSeriesDefaults().getRendererOptions();
			((PlotBarRendererOptions)rendererOptions).setVaryBarColor(true);
			for (K key : keysMap.keySet()) {
				Color color = seriesColorFunction.apply(key);
				options.getSeriesColors().add(ChartColors.toCssString(color));
				Color negativeColor = negativeSeriesColorFunction.apply(key);
				options.getNegativeSeriesColors().add(ChartColors.toCssString(negativeColor));
			}
		}
	}
	
	public static <S> IJQPlotConfigurer<S, Object> seriesColors(SerializableFunction2<? super S, ? extends Color> seriesColorFunction) {
		return seriesColors(seriesColorFunction, seriesColorFunction);
	}
	
	public static <S> IJQPlotConfigurer<S, Object> seriesColors(SerializableFunction2<? super S, ? extends Color> seriesColorFunction,
			SerializableFunction2<? super S, ? extends Color> negativeSeriesColorFunction) {
		return new SeriesColorsFunctionJQPlotConfigurer<>(seriesColorFunction, negativeSeriesColorFunction);
	}
	private static class SeriesColorsFunctionJQPlotConfigurer<S> extends AbstractJQPlotConfigurer<S, Object> {
		private static final long serialVersionUID = -7455227724829756556L;
		
		private final SerializableFunction2<? super S, ? extends Color> seriesColorFunction;
		
		private final SerializableFunction2<? super S, ? extends Color> negativeSeriesColorFunction;
		
		public SeriesColorsFunctionJQPlotConfigurer(SerializableFunction2<? super S, ? extends Color> seriesColorFunction,
				SerializableFunction2<? super S, ? extends Color> negativeSeriesColorFunction) {
			super();
			this.seriesColorFunction = seriesColorFunction;
			this.negativeSeriesColorFunction = negativeSeriesColorFunction;
		}

		@Override
		public void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap, Map<?, PlotTick> keysMap, Locale locale) {
			for (Map.Entry<? extends S, PlotSeries> seriesEntry : seriesMap.entrySet()) {
				Color color = seriesColorFunction.apply(seriesEntry.getKey());
				if (color != null) {
					seriesEntry.getValue().setColor(ChartColors.toCssString(color));
				}
				Color negativeColor = negativeSeriesColorFunction.apply(seriesEntry.getKey());
				if (negativeColor != null) {
					seriesEntry.getValue().setNegativeColor(ChartColors.toCssString(negativeColor));
				}
			}
		}
	}
	
	public static IJQPlotConfigurer<Object, Object> seriesColors(Iterable<? extends Color> colors) {
		return seriesColors(colors, colors);
	}
	
	public static IJQPlotConfigurer<Object, Object> seriesColors(Iterable<? extends Color> colors, Iterable<? extends Color> negativeColors) {
		return new SeriesColorsListJQPlotConfigurer(colors, negativeColors);
	}
	private static class SeriesColorsListJQPlotConfigurer extends AbstractJQPlotConfigurer<Object, Object> {
		private static final long serialVersionUID = -7455227724829756556L;
		
		private final List<String> colors;
		
		private final List<String> negativeColors;
		
		public SeriesColorsListJQPlotConfigurer(Iterable<? extends Color> colors, Iterable<? extends Color> negativeColors) {
			super();
			this.colors = Lists.newArrayList();
			for (Color color : colors) {
				this.colors.add(ChartColors.toCssString(color));
			}
			this.negativeColors = Lists.newArrayList();
			for (Color color : negativeColors) {
				this.negativeColors.add(ChartColors.toCssString(color));
			}
		}
		
		@Override
		public void configure(PlotOptions options, Map<? extends Object, PlotSeries> seriesMap,
				Map<? extends Object, PlotTick> keysMap, Locale locale) {
			options.setSeriesColors(Collections.unmodifiableList(colors));
			options.setNegativeSeriesColors(Collections.unmodifiableList(negativeColors));
		}
	}

	public static <S> IJQPlotConfigurer<Object, Object> seriesLabelsDisabled() {
		return SERIES_LABELS_DISABLED;
	}
	private static final IJQPlotConfigurer<Object, Object> SERIES_LABELS_DISABLED = new AbstractJQPlotConfigurer<Object, Object>() {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void configure(PlotOptions options, Map<? extends Object, PlotSeries> seriesMap,
				Map<? extends Object, PlotTick> keysMap, Locale locale) {
			options.getHighlighter().setFormatString("%2$s");
			options.getLegend().setShow(false);
		}
	};

	public static <S> IJQPlotConfigurer<S, Object> seriesLabels(IConverter<? super S> serieLabelConverter) {
		return new SeriesLabelsJQPlotConfigurer<>(serieLabelConverter);
	}
	private static class SeriesLabelsJQPlotConfigurer<S> extends AbstractJQPlotConfigurer<S, Object> {
		private static final long serialVersionUID = -7455227724829756556L;
		
		private final IConverter<? super S> serieLabelConverter;
		
		public SeriesLabelsJQPlotConfigurer(IConverter<? super S> serieLabelConverter) {
			super();
			this.serieLabelConverter = serieLabelConverter;
		}

		@Override
		public void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap, Map<?, PlotTick> keysMap, Locale locale) {
			for (Map.Entry<? extends S, PlotSeries> seriesEntry : seriesMap.entrySet()) {
				seriesEntry.getValue().setLabel(serieLabelConverter.convertToString(seriesEntry.getKey(), locale));
			}
		}
	}

	public static IJQPlotConfigurer<Object, Object> yAxisFloatFormat() {
		return Y_AXIS_FLOAT_FORMAT;
	}
	private static final IJQPlotConfigurer<Object, Object> Y_AXIS_FLOAT_FORMAT = new AbstractJQPlotConfigurer<Object, Object>() {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void configure(PlotOptions options, Map<?, PlotSeries> seriesMap, Map<?, PlotTick> keysMap, Locale locale) {
			options.getAxes().getYaxis()
					.getTickOptions().setFormatString("%.1f");
			options.getAxes().getYaxis()
					.setMax(null).setTickInterval(null);
		}
	};

	public static <V extends Number> IJQPlotConfigurer<Object, Object> yAxisWindow(V min, V max) {
		return new YAxisWindowJQPlotConfigurer<>(min, max);
	}
	private static class YAxisWindowJQPlotConfigurer<V extends Number> extends AbstractJQPlotConfigurer<Object, Object> {
		private static final long serialVersionUID = 1L;
		
		private final V min;
		private final V max;

		public YAxisWindowJQPlotConfigurer(V min, V max) {
			super();
			this.min = min;
			this.max = max;
		}
		
		@Override
		public void configure(PlotOptions options, Map<?, PlotSeries> seriesMap, Map<?, PlotTick> keysMap, Locale locale) {
			options.getAxes().getYaxis()
					.setMin(min).setMax(max);
		}
	};

	public static IJQPlotConfigurer<Object, Object> xAxisLabel(String labelKey) {
		return new XAxisLabelJQPlotConfigurer(labelKey);
	}
	private static class XAxisLabelJQPlotConfigurer extends AbstractJQPlotConfigurer<Object, Object> {
		private static final long serialVersionUID = 1L;
		
		private final String labelKey;

		public XAxisLabelJQPlotConfigurer(String labelKey) {
			super();
			this.labelKey = labelKey;
		}
		
		@Override
		public void configure(PlotOptions options, Map<?, PlotSeries> seriesMap, Map<?, PlotTick> keysMap, Locale locale) {
			options.getAxes().getXaxis()
					.setLabel(Localizer.get().getString(labelKey, null, null, locale, null, (String) null));
		}
	};

	public static IJQPlotConfigurer<Object, Object> yAxisLabel(String labelKey) {
		return new YAxisLabelJQPlotConfigurer(labelKey);
	}
	private static class YAxisLabelJQPlotConfigurer extends AbstractJQPlotConfigurer<Object, Object> {
		private static final long serialVersionUID = 1L;
		
		private final String labelKey;

		public YAxisLabelJQPlotConfigurer(String labelKey) {
			super();
			this.labelKey = labelKey;
		}
		
		@Override
		public void configure(PlotOptions options, Map<?, PlotSeries> seriesMap, Map<?, PlotTick> keysMap, Locale locale) {
			options.getAxes().getYaxis()
					.setLabel(Localizer.get().getString(labelKey, null, null, locale, null, (String) null));
		}
	}

	public static IJQPlotConfigurer<Object, Object> yAxisDisabled() {
		return Y_AXIS_DISABLED;
	}
	private static final IJQPlotConfigurer<Object, Object> Y_AXIS_DISABLED = new AbstractJQPlotConfigurer<Object, Object>() {
		private static final long serialVersionUID = 1L;

		@Override
		public void configure(PlotOptions options, Map<? extends Object, PlotSeries> seriesMap,
				Map<? extends Object, PlotTick> keysMap, Locale locale) {
			options.getAxes().getYaxis()
					.setRendererOptions(new PlotAxisRendererOptions() {
						private static final long serialVersionUID = 1L;
						@SuppressWarnings("unused")
						private Boolean drawBaseline = false; // COUNTOURNEMENT BUG JQPLOT
					})
					.setShow(false)
					.getTickOptions().setShowMark(false).setShowGridline(false);
		}
	};

	public static IJQPlotConfigurer<Object, Object> yAxisGridlinesOnly() {
		return Y_AXIS_GRIDLINES_ONLY;
	}
	private static final IJQPlotConfigurer<Object, Object> Y_AXIS_GRIDLINES_ONLY = new AbstractJQPlotConfigurer<Object, Object>() {
		private static final long serialVersionUID = 1L;
		@Override
		public void configure(PlotOptions options, Map<? extends Object,PlotSeries> seriesMap,
				Map<? extends Object,PlotTick> keysMap, Locale locale) {
			options.getAxes().getYaxis()
					.setRendererOptions(new PlotAxisRendererOptions() {
						private static final long serialVersionUID = 1L;
						@SuppressWarnings("unused")
						private Boolean drawBaseline = false; // COUNTOURNEMENT BUG JQPLOT
					})
					.setShow(false)
					.getTickOptions().setShowMark(false);
//			options.getAxes().getYaxis()
//					.setTicks((List<PlotTick>)null);
		}
	};

	public static IJQPlotConfigurer<Object, Object> xAxisDisabled() {
		return X_AXIS_DISABLED;
	}
	private static final IJQPlotConfigurer<Object, Object> X_AXIS_DISABLED = new AbstractJQPlotConfigurer<Object, Object>() {
		private static final long serialVersionUID = 1L;

		@Override
		public void configure(PlotOptions options, Map<? extends Object, PlotSeries> seriesMap,
				Map<? extends Object, PlotTick> keysMap, Locale locale) {
			options.getAxes().getXaxis()
					.setShow(false)
					.getTickOptions().setShow(false).setShowGridline(false);
		}
	};

}
