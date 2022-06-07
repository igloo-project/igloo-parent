package org.iglooproject.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.more.jqplot.util.LabelledSeries;
import org.iglooproject.wicket.more.jqplot.util.LabelledSeriesEntry;
import org.iglooproject.wicket.renderer.Renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import nl.topicus.wqplot.data.AbstractSeries;
import nl.topicus.wqplot.data.Series;
import nl.topicus.wqplot.data.SeriesEntry;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;
import nl.topicus.wqplot.options.PlotTooltipAxes;

public final class JQPlotDataAdapters {
	
	private JQPlotDataAdapters() { }
	
	public static <S, K, V extends Number, TK, SE extends SeriesEntry<TK, V>> IJQPlotDataAdapter<S, K, V, TK> addPercentLabels(final IJQPlotDataAdapter<S, K, V, TK> adapter, final Renderer<? super Double> percentRenderer) {
		return new ForwardingJQPlotDataAdapter<S, K, V, TK>(adapter) {	
			private static final long serialVersionUID = 1L;
			@Override
			protected Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> transformOnGet(Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> delegateObject, Locale locale) {
				return addPercentLabels(delegateObject, percentRenderer, locale);
			}
			@Override
			public void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap,
					Map<? extends K, PlotTick> keysMap, Locale locale) {
				options.getHighlighter()
						.setTooltipAxes(PlotTooltipAxes.y)
						.setYvalues(2)
						.setFormatString("%s - %s (%s)")
						.setUseAxesFormatters(true);
			}
		};
	}
	
	private static <K, V extends Number, TK> Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> addPercentLabels(
			Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> delegateObject, Renderer<? super Double> percentRenderer, Locale locale) {
		final Map<TK, Double> sums = Maps.newHashMap();
		for (AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>> series : delegateObject) {
			for (SeriesEntry<TK, V> entry : series.getData()) {
				TK key = entry.getKey();
				Double currentSum = sums.get(key);
				if (currentSum == null) {
					currentSum = 0.0;
				}
				V currentValue = entry.getValue();
				if (currentValue != null) {
					currentSum += currentValue.doubleValue();
				}
				sums.put(key, currentSum);
			}
		}
		
		Renderer<SeriesEntry<TK, V>> entryRenderer = percentRenderer.onResultOf(
			(SerializableFunction2<SeriesEntry<TK, V>, Double>)
				entry -> {
					V absoluteValue = entry.getValue();
					if (absoluteValue == null) {
						return null;
					} else {
						return absoluteValue.doubleValue() / sums.get(entry.getKey());
					}
				}
		);
		
		return addLabels(delegateObject, entryRenderer, locale);
	}
	
	private static <K, V extends Number, E extends SeriesEntry<K, V>> Collection<LabelledSeries<K, V>> addLabels(
			Collection<? extends AbstractSeries<? extends K, V, ? extends E>> input, Renderer<? super E> renderer, Locale locale) {
		List<LabelledSeries<K, V>> seriesList = Lists.newArrayListWithCapacity(input.size());
		for (Series<?, ?, ? extends E> series : input) {
			LabelledSeries<K, V> copiedSeries = new LabelledSeries<>();
			seriesList.add(copiedSeries);
			for (E entry : series.getData()) {
				copiedSeries.addEntry(new LabelledSeriesEntry<K, V>(entry.getKey(), entry.getValue(), renderer.render(entry, locale)));
			}
		}
		return seriesList;
	}
	
	public static <S, K, V extends Number & Comparable<V>, TK, SE extends SeriesEntry<TK, V>> IJQPlotDataAdapter<S, K, V, TK> fix(final IJQPlotDataAdapter<S, K, V, TK> adapter) {
		return new ForwardingJQPlotDataAdapter<S, K, V, TK>(adapter) {
			private static final long serialVersionUID = 1L;
			@Override
			public void afterConfigure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap,
					Map<? extends K, PlotTick> keysMap, Locale locale) {
				// jqPlot bug workaround
				// To avoid the problems related to data too low: jqPlot duplicates the values on the Y axis when
				// we use an integer format and that the differences are decimal.
				String formatString = options.getAxes().getYaxis().getTickOptions().getFormatString();
				if ("%d".equals(formatString) || "%'d".equals(formatString)) {
					Collection<? extends V> values = getValues();
					double min = values.isEmpty() ? 0.0 : Ordering.natural().min(values).doubleValue();
					double max = values.isEmpty() ? 1.0 : Ordering.natural().max(values).doubleValue();
					Boolean fillToZero = options.getSeriesDefaults().getFillToZero();
					if ((max - min) <= 5.0) {
						options.getAxes().getYaxis()
								.setMin(0.0 <= min && max <= 5.0 || fillToZero != null && fillToZero
										? 0 : (int) min - 2)
								.setMax((int) max + 2)
								.setNumberTicks(5);
					} else {
						options.getAxes().getYaxis()
								.setMin(null)
								.setMax(null)
								.setNumberTicks(null);
					}
				}
				
				super.configure(options, seriesMap, keysMap, locale);
			}
			
			@Override
			protected Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> transformOnGet(
					Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> delegateObject, Locale locale) {
				// jqPlot bug workaround
				// jqPlot doesn't work if we give it an empty series or null ("No data specified")
				// The only way to make it accept the fact that we want to display a series (for instance in the legend) even
				// if there is no data is to add a null entry in this series.
				for (AbstractSeries<?, ?, ?> series : delegateObject) {
					if (series.getData().isEmpty()) {
						series.addEntry(null); 
					}
				}
				
				return delegateObject;
			}
		};
	}
}
