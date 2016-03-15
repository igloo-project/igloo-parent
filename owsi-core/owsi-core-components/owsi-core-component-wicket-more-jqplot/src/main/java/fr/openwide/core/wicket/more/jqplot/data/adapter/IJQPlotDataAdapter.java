package fr.openwide.core.wicket.more.jqplot.data.adapter;

import java.util.Collection;

import org.apache.wicket.model.IComponentAssignedModel;

import fr.openwide.core.wicket.more.jqplot.config.IJQPlotConfigurer;
import fr.openwide.core.wicket.more.jqplot.data.provider.IJQPlotDataProvider;
import nl.topicus.wqplot.data.AbstractSeries;

public interface IJQPlotDataAdapter<S, K, V> extends IJQPlotDataProvider<S, K, V>,
		IJQPlotConfigurer<S, K>,
		IComponentAssignedModel<Collection<? extends AbstractSeries<?, V, ?>>> {

	Collection<S> getSeriesTicks();
	
	Collection<K> getKeysTicks();
	
	@Override
	Collection<? extends AbstractSeries<?, V, ?>> getObject();

}
