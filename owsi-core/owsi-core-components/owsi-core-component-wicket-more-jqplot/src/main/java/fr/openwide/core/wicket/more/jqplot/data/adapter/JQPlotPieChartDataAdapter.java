package fr.openwide.core.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.jqplot.data.provider.IJQPlotDataProvider;
import fr.openwide.core.wicket.more.rendering.Renderer;
import nl.topicus.wqplot.data.StringNumberSeries;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;

/**
 * Adapter for the pie charts data.
 * <p>
 * Features:
 * <ul>
 * <li>displays automatically all the series, even the ones without data
 * <li>integrate the value in the form [[['label',value],['label2',value3]]], as it's the only way to associate
 *     correct labels to the pie charts data.
 * </ul>
 */
public class JQPlotPieChartDataAdapter<S, K, V extends Number & Comparable<V>> extends AbstractMissingValuesJQPlotDataAdapter<S, K, V> {
	
	private static final long serialVersionUID = 3961697302069579609L;
	
	private final Renderer<? super K> keyRenderer;

	public JQPlotPieChartDataAdapter(
			IJQPlotDataProvider<S, K, V> dataProvider,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel,
			Renderer<? super K> keyRenderer) {
		super(dataProvider, seriesModel, keysModel);
		this.keyRenderer = keyRenderer;
	}
	
	@Override
	public Collection<StringNumberSeries<V>> getObject(Locale locale) {
		Iterable<? extends K> keys = getKeys();
		
		Collection<StringNumberSeries<V>> result = Lists.newArrayList();
		for (S series : getSeries()) {
			StringNumberSeries<V> seriesData = createSeriesData(locale, series, keys);
			result.add(seriesData);
		}
		return result;
	}
	
	private StringNumberSeries<V> createSeriesData(Locale locale, S series, Iterable<? extends K> keys) {
		StringNumberSeries<V> seriesData = new StringNumberSeries<V>();
		for (K key : keys) {
			@Nullable V value = dataProvider.getValue(series, key);
			seriesData.addEntry(keyRenderer.render(key, locale), value);
		}
		return seriesData;
	}
	
	@Override
	public void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap,
			Map<? extends K, PlotTick> keysMap, Locale locale) {
		// Is only so that we don't serialize the keys.
		for (Map.Entry<? extends K, PlotTick> entry : keysMap.entrySet()) {
			K key = entry.getKey();
			PlotTick tick = entry.getValue();
			tick.setVal(keyRenderer.render(key, locale));
		}
	}
}
