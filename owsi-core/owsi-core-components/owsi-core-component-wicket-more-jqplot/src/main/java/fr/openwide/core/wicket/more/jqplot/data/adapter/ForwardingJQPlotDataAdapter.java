package fr.openwide.core.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

import nl.topicus.wqplot.data.AbstractSeries;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;

public abstract class ForwardingJQPlotDataAdapter<S, K, V> extends AbstractJQPlotDataAdapter<S, K, V>
		implements IJQPlotDataAdapter<S, K, V> {

	private static final long serialVersionUID = 773447284220394712L;
	
	private final IJQPlotDataAdapter<S, K, V> delegate;
	
	public ForwardingJQPlotDataAdapter(IJQPlotDataAdapter<S, K, V> delegate) {
		super(delegate);
		this.delegate = delegate;
	}
	
	@Override
	protected Collection<? extends AbstractSeries<?, V, ?>> getObject(Locale locale) {
		return transformOnGet(delegate.getObject(), locale);
	}
	
	protected Collection<? extends AbstractSeries<?, V, ?>> transformOnGet(Collection<? extends AbstractSeries<?, V, ?>> delegateObject, Locale locale) {
		return delegateObject;
	}
	
	@Override
	public final void setObject(Collection<? extends AbstractSeries<?, V, ?>> object) {
		delegate.setObject(transformOnSet(object));
	}
	
	protected Collection<? extends AbstractSeries<?, V, ?>> transformOnSet(Collection<? extends AbstractSeries<?, V, ?>> setObject) {
		return setObject;
	}

	@Override
	public void configure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap,
			Map<? extends K, PlotTick> keysMap, Locale locale) {
		delegate.configure(options, seriesMap, keysMap, locale);
	}

	@Override
	public void detach() {
		delegate.detach();
	}
	
	@Override
	public void afterConfigure(PlotOptions options, Map<? extends S, PlotSeries> seriesMap,
			Map<? extends K, PlotTick> keysMap, Locale locale) {
		delegate.afterConfigure(options, seriesMap, keysMap, locale);
	}

	@Override
	public Collection<S> getSeries() {
		return delegate.getSeries();
	}

	@Override
	public Collection<K> getKeys() {
		return delegate.getKeys();
	}
	
	@Override
	public V getValue(S serie, K key) {
		return delegate.getValue(serie, key);
	}
	
	@Override
	public Collection<S> getSeriesTicks() {
		return delegate.getSeriesTicks();
	}
	
	@Override
	public Collection<K> getKeysTicks() {
		return delegate.getKeysTicks();
	}
	
	@Override
	public IWrapModel<Collection<? extends AbstractSeries<?, V, ?>>> wrapOnAssignment(Component component) {
		return new WrapModel(component);
	}
	
	private class WrapModel implements IWrapModel<Collection<? extends AbstractSeries<?, V, ?>>> {
		
		private static final long serialVersionUID = 6641833381429869435L;
		
		private final Component component;
		
		private final IModel<Collection<? extends AbstractSeries<?, V, ?>>> wrappedDelegate;
		
		public WrapModel(Component component) {
			super();
			this.component = component;
			this.wrappedDelegate = delegate.wrapOnAssignment(component);
		}

		@Override
		public Collection<? extends AbstractSeries<?, V, ?>> getObject() {
			return ForwardingJQPlotDataAdapter.this.transformOnGet(wrappedDelegate.getObject(), component.getLocale());
		}

		@Override
		public void setObject(Collection<? extends AbstractSeries<?, V, ?>> object) {
			wrappedDelegate.setObject(ForwardingJQPlotDataAdapter.this.transformOnSet(object));
		}

		@Override
		public void detach() {
			wrappedDelegate.detach();
			ForwardingJQPlotDataAdapter.this.detach();
		}

		@Override
		public IModel<?> getWrappedModel() {
			return ForwardingJQPlotDataAdapter.this;
		}
		
	}

}
