package fr.openwide.core.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import fr.openwide.core.wicket.more.jqplot.data.provider.IJQPlotDataProvider;
import fr.openwide.core.wicket.more.jqplot.data.provider.JQPlotMapDataProvider;
import fr.openwide.core.wicket.more.jqplot.data.provider.JQPlotTableDataProvider;
import nl.topicus.wqplot.components.plugins.JQPlotCategoryAxisRenderer;
import nl.topicus.wqplot.data.NumberSeries;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;

/**
 * Adapter for data categorized on the X axis.
 * <p>
 * Features:
 * <ul>
 * <li>Display automatically all the series even the ones without data
 * </ul>
 */
public class JQPlotCategoryKeyDataAdapter<S, K, V extends Number & Comparable<V>> extends AbstractMissingValuesJQPlotDataAdapter<S, K, V> {
	
	private static final long serialVersionUID = 3961697302069579609L;

	public JQPlotCategoryKeyDataAdapter(
			IModel<Table<S, K, V>> tableModel,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel) {
		this(new JQPlotTableDataProvider<>(tableModel), seriesModel, keysModel);
	}

	public JQPlotCategoryKeyDataAdapter(
			IModel<Map<K, V>> tableModel,
			IModel<? extends Collection<? extends K>> keysModel) {
		this(new JQPlotMapDataProvider<S, K, V>(tableModel), null, keysModel);
	}

	public JQPlotCategoryKeyDataAdapter(
			IJQPlotDataProvider<S, K, V> dataProvider,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel) {
		super(dataProvider, seriesModel, keysModel);
	}

	@Override
	public void initialize(PlotOptions options) {
		options.getAxes().getXaxis()
				.setRenderer(JQPlotCategoryAxisRenderer.get());
		options.getAxes().getXaxis().getTickOptions().setShowGridline(false);
	}

	@Override
	public void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap, Map<? extends K, PlotTick> keysMap, Locale locale) {
		for (PlotSeries series : seriesMap.values()) {
			series.setBreakOnNull(true); // In the case of categories, we shouldn't link points separated by a null value.
		}
	}

	@Override
	public Collection<NumberSeries<Integer, V>> getObject(Locale locale) {
		Iterable<? extends K> keys = getKeys();
		
		Collection<NumberSeries<Integer, V>> result = Lists.newArrayList();
		for (S series : getSeries()) {
			NumberSeries<Integer, V> seriesData = createSeriesData(series, keys);
			result.add(seriesData);
		}
		return result;
	}
	
	private NumberSeries<Integer, V> createSeriesData(S series, Iterable<? extends K> keys) {
		NumberSeries<Integer, V> seriesData = new NumberSeries<Integer, V>();
		int index = 1; // Categories index starts at 1 for jqPlot
		for (K key : keys) {
			@Nullable V value = dataProvider.getValue(series, key);
			seriesData.addEntry(index, value);
			++index;
		}
		return seriesData;
	}
}
