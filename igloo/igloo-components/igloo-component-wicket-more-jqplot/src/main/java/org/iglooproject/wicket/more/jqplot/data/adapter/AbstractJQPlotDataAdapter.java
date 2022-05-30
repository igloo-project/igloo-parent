package org.iglooproject.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.util.lang.Classes;
import org.iglooproject.wicket.api.util.Detachables;
import org.iglooproject.wicket.more.jqplot.data.provider.AbstractJQPlotDataProvider;
import org.iglooproject.wicket.more.jqplot.data.provider.IJQPlotDataProvider;

import nl.topicus.wqplot.data.AbstractSeries;
import nl.topicus.wqplot.data.SeriesEntry;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;

public abstract class AbstractJQPlotDataAdapter<S, K, V, TK> extends AbstractJQPlotDataProvider<S, K, V>
		implements IJQPlotDataAdapter<S, K, V, TK> {
	
	private static final long serialVersionUID = 51423240690775236L;
	
	private final IJQPlotDataProvider<S, K, V> dataProvider;
	
	public AbstractJQPlotDataAdapter(IJQPlotDataProvider<S, K, V> dataProvider) {
		super();
		this.dataProvider = dataProvider;
	}
	
	@Override
	public final Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> getObject() {
		return getObject(Session.get().getLocale());
	}
	
	protected abstract Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> getObject(Locale locale);

	@Override
	public void setObject(Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> object) {
		setObject(object, Session.get().getLocale());
	}

	protected void setObject(Collection<? extends AbstractSeries<?, ?, ?>> object, Locale locale) {
		throw new UnsupportedOperationException("Cannot set the value of a " + Classes.simpleName(getClass()));
	}
	
	@Override
	public Collection<S> getSeries() {
		return dataProvider.getSeries();
	}
	
	@Override
	public Collection<K> getKeys() {
		return dataProvider.getKeys();
	}
	
	@Override
	public V getValue(S serie, K key) {
		return dataProvider.getValue(serie, key);
	}
	
	@Override
	public Collection<V> getValues() {
		return dataProvider.getValues();
	}
	
	@Override
	public Collection<S> getSeriesTicks() {
		return getSeries();
	}
	
	@Override
	public Collection<K> getKeysTicks() {
		return getKeys();
	}

	@Override
	public void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap,
			Map<? extends K, PlotTick> keysMap, Locale locale) {
		// Does nothing.
		// To be overriden by subclasses.
	}
	
	@Override
	public void afterConfigure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap,
			Map<? extends K, PlotTick> keysMap, Locale locale) {
		// Does nothing.
		// To be overriden by subclasses.
	}
	
	@Override
	public void detach() {
		Detachables.detach(dataProvider);
	}
	
	@Override
	public IWrapModel<Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>>> wrapOnAssignment(Component component) {
		return new WrapModel(component);
	}
	
	protected class WrapModel implements IWrapModel<Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>>> {
		private static final long serialVersionUID = -3127520838702743514L;
		
		private final Component component;
		
		public WrapModel(Component component) {
			super();
			this.component = component;
		}

		@Override
		public Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> getObject() {
			return AbstractJQPlotDataAdapter.this.getObject(component.getLocale());
		}

		@Override
		public void setObject(Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> object) {
			AbstractJQPlotDataAdapter.this.setObject(object, component.getLocale());
		}

		@Override
		public void detach() {
			AbstractJQPlotDataAdapter.this.detach();
		}

		@Override
		public IModel<?> getWrappedModel() {
			return AbstractJQPlotDataAdapter.this;
		}
	}

}
