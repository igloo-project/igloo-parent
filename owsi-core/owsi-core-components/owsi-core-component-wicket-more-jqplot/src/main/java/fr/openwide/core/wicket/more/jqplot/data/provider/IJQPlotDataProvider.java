package fr.openwide.core.wicket.more.jqplot.data.provider;

import java.util.Collection;

import org.apache.wicket.model.IDetachable;

public interface IJQPlotDataProvider<S, K, V> extends IDetachable {
	
	V getValue(S serie, K key);
	
	Collection<S> getSeries();
	
	Collection<K> getKeys();

	Collection<V> getValues();

	Collection<V> getSeriesValues(S series);

	Collection<V> getKeyValues(K key);

}
