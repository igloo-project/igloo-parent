package fr.openwide.core.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import fr.openwide.core.wicket.more.jqplot.data.provider.IJQPlotDataProvider;
import fr.openwide.core.wicket.more.jqplot.data.provider.JQPlotMapDataProvider;
import fr.openwide.core.wicket.more.jqplot.data.provider.JQPlotTableDataProvider;
import nl.topicus.wqplot.components.plugins.JQPlotDateAxisRenderer;
import nl.topicus.wqplot.data.BaseSeries;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;

/**
 * Adapter for data categorized with dates on the X axis
 * <p>
 * Display automatically all the series even the ones without data.
 */
public class JQPlotDateKeyDataAdapter<S, K extends Date, V extends Number> extends AbstractMissingValuesJQPlotDataAdapter<S, K, V> {
	
	private static final long serialVersionUID = 3961697302069579609L;

	public JQPlotDateKeyDataAdapter(
			IModel<Table<S, K, V>> tableModel,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel) {
		this(new JQPlotTableDataProvider<>(tableModel), seriesModel, keysModel);
	}

	public JQPlotDateKeyDataAdapter(
			IModel<Map<K, V>> tableModel,
			IModel<? extends Collection<? extends K>> keysModel) {
		this(new JQPlotMapDataProvider<S, K, V>(tableModel), null, keysModel);
	}
	
	public JQPlotDateKeyDataAdapter(
			IJQPlotDataProvider<S, K, V> dataProvider,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel) {
		super(dataProvider, seriesModel, keysModel);
	}

	@Override
	public void initialize(PlotOptions options) {
		options.getAxes().getXaxis()
				.setRenderer(JQPlotDateAxisRenderer.get());
		options.getAxes().getXaxis().getTickOptions().setShowGridline(true);
	}

	@Override
	public void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap, Map<? extends K, PlotTick> keysMap, Locale locale) {
		// Nothing to do here
	}

	@Override
	public Collection<BaseSeries<K, V>> getObject(Locale locale) {
		Iterable<? extends K> keys = getKeys();
		
		Collection<BaseSeries<K, V>> result = Lists.newArrayList();
		for (S series : getSeries()) {
			BaseSeries<K, V> seriesData = createSeriesData(series, keys);
			result.add(seriesData);
		}
		return result;
	}
	
	private BaseSeries<K, V> createSeriesData(S series, Iterable<? extends K> keys) {
		BaseSeries<K, V> seriesData = new BaseSeries<K, V>();
		for (K key : keys) {
			@Nullable V value = dataProvider.getValue(series, key);
			seriesData.addEntry(key, value);
		}
		return seriesData;
	}
}
