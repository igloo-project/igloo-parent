package fr.openwide.core.wicket.more.jqplot.component;

import java.util.Collection;

import org.apache.wicket.model.IModel;

import com.google.common.collect.Table;

import fr.openwide.core.wicket.more.jqplot.config.AbstractJQPlotConfigurer;
import fr.openwide.core.wicket.more.jqplot.data.adapter.IJQPlotDataAdapter;
import fr.openwide.core.wicket.more.jqplot.data.adapter.JQPlotCategoryKeyDataAdapter;
import fr.openwide.core.wicket.more.jqplot.data.adapter.JQPlotDataAdapters;
import fr.openwide.core.wicket.more.jqplot.data.provider.IJQPlotDataProvider;
import fr.openwide.core.wicket.more.jqplot.data.provider.JQPlotTableDataProvider;
import nl.topicus.wqplot.components.plugins.JQPlotBarRenderer;
import nl.topicus.wqplot.options.PlotOptions;

public class JQPlotStackedBarsPanel<S, K, V extends Number & Comparable<V>> extends JQPlotPanel<S, K, V> {

	private static final long serialVersionUID = -5575918534912813908L;

	public JQPlotStackedBarsPanel(String id,
			IModel<Table<S, K, V>> tableModel,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel) {
		this(id, new JQPlotTableDataProvider<>(tableModel), seriesModel, keysModel);
	}

	public JQPlotStackedBarsPanel(String id, IJQPlotDataProvider<S, K, V> dataProvider) {
		this(id, dataProvider, null, null);
	}

	public JQPlotStackedBarsPanel(String id,
			IJQPlotDataProvider<S, K, V> dataProvider,
			IModel<? extends Collection<? extends S>> seriesModel,
			IModel<? extends Collection<? extends K>> keysModel) {
		this(id, new JQPlotCategoryKeyDataAdapter<>(dataProvider, seriesModel, keysModel));
	}
	
	public JQPlotStackedBarsPanel(String id, IJQPlotDataAdapter<S, K, V> dataAdapter) {
		super(id, JQPlotDataAdapters.fix(dataAdapter));
		
		add(new AbstractJQPlotConfigurer<S, K>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void initialize(PlotOptions options) {
				options.setStackSeries(true);
				options.getSeriesDefaults()
						.setFillToZero(true)
						.setRenderer(JQPlotBarRenderer.get())
						.setRendererOptions(getOptionsFactory().newPlotStackedBarRendererOptions());
			}
		});
	}
}
