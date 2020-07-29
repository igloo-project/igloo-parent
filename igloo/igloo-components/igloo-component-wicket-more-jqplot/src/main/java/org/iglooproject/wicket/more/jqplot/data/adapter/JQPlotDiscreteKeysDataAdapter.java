package org.iglooproject.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.iglooproject.commons.util.collections.range.time.DateDiscreteDomain;
import org.iglooproject.commons.util.collections.range.time.PartitionDiscreteDomain;
import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.wicket.more.jqplot.config.JQPlotConfigurers;
import org.iglooproject.wicket.more.jqplot.data.provider.IJQPlotDataProvider;
import org.iglooproject.wicket.more.jqplot.data.provider.JQPlotMapDataProvider;
import org.iglooproject.wicket.more.jqplot.data.provider.JQPlotTableDataProvider;
import org.iglooproject.wicket.more.model.ContiguousSetModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import nl.topicus.wqplot.components.plugins.JQPlotCategoryAxisRenderer;
import nl.topicus.wqplot.data.NumberSeries;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;

/**
 * Adapter for data with discrete keys.
 * <p>This adapter assigns an index to each data point so that all charts (especially bar charts) will render correctly.
 * <p>This is only useful if your data references keys from a discrete domain (i.e. with a small, finite number of values
 * in a given range). This is the case when your keys are labels ("male"/"female"), but also when they are ranges
 * (e.g. the first day of each week at midnight, which represents the whole week).
 * <p>If your data keys are only "almost-continuous" keys (such as floating-point numbers, or dates precise
 * to the second, use a {@link AbstractJQPlotContinuousKeysDataAdapter} instead.
 * 
 * @see AbstractJQPlotContinuousKeysDataAdapter
 * @see ContiguousSetModel
 * @see PartitionDiscreteDomain
 * @see DateDiscreteDomain
 * @see JQPlotConfigurers#xAxisExplicitTicks(IConverter)
 */
public class JQPlotDiscreteKeysDataAdapter<S, K, V extends Number> extends AbstractMissingValuesJQPlotDataAdapter<S, K, V, Integer> {
	
	private static final long serialVersionUID = 3961697302069579609L;
	
	private final IRenderer<? super K> keyRenderer;

	public JQPlotDiscreteKeysDataAdapter(
			IModel<Table<S, K, V>> tableModel,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel,
			IRenderer<? super K> keyRenderer) {
		this(new JQPlotTableDataProvider<>(tableModel), seriesModel, keysModel, keyRenderer);
	}

	public JQPlotDiscreteKeysDataAdapter(
			IModel<Table<S, K, V>> tableModel,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel,
			IModel<? extends V> missingValueReplacementModel,
			IRenderer<? super K> keyRenderer) {
		this(new JQPlotTableDataProvider<>(tableModel), seriesModel, keysModel, missingValueReplacementModel, keyRenderer);
	}

	public JQPlotDiscreteKeysDataAdapter(
			IModel<Map<K, V>> mapModel,
			IModel<? extends Collection<? extends K>> keysModel,
			IRenderer<? super K> keyRenderer) {
		this(new JQPlotMapDataProvider<S, K, V>(mapModel), null, keysModel, keyRenderer);
	}

	public JQPlotDiscreteKeysDataAdapter(
			IJQPlotDataProvider<S, K, V> dataProvider,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel,
			IRenderer<? super K> keyRenderer) {
		this(dataProvider, seriesModel, keysModel, null, keyRenderer);
	}

	public JQPlotDiscreteKeysDataAdapter(
			IJQPlotDataProvider<S, K, V> dataProvider,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel,
			IModel<? extends V> missingValueReplacementModel,
			IRenderer<? super K> keyRenderer) {
		super(dataProvider, seriesModel, keysModel, missingValueReplacementModel);
		this.keyRenderer = keyRenderer;
	}

	@Override
	public void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap, Map<? extends K, PlotTick> keysMap, Locale locale) {
		options.getAxes().getXaxis()
				.setRenderer(JQPlotCategoryAxisRenderer.get());
		for (Map.Entry<? extends K, PlotTick> tickEntry : keysMap.entrySet()) {
			tickEntry.getValue().setVal(keyRenderer.render(tickEntry.getKey(), locale));
		}
	}

	@Override
	public Collection<NumberSeries<Integer, V>> getObject(Locale locale) {
		Iterable<? extends K> keysTicks = getKeysTicks();
		
		Collection<NumberSeries<Integer, V>> result = Lists.newArrayList();
		for (S series : getSeriesTicks()) {
			NumberSeries<Integer, V> seriesData = createSeriesData(series, keysTicks);
			result.add(seriesData);
		}
		return result;
	}
	
	private NumberSeries<Integer, V> createSeriesData(S series, Iterable<? extends K> keys) {
		NumberSeries<Integer, V> seriesData = new NumberSeries<>();
		int index = 1; // Categories index starts at 1 for jqPlot
		for (K key : keys) {
			V value = getValue(series, key);
			seriesData.addEntry(index, value);
			++index;
		}
		return seriesData;
	}
}
