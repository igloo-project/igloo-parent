package org.iglooproject.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import nl.topicus.wqplot.data.AbstractSeries;
import nl.topicus.wqplot.data.SeriesEntry;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

public abstract class ForwardingJQPlotDataAdapter<S, K, V, TK>
    extends AbstractJQPlotDataAdapter<S, K, V, TK> implements IJQPlotDataAdapter<S, K, V, TK> {

  private static final long serialVersionUID = 773447284220394712L;

  private final IJQPlotDataAdapter<S, K, V, TK> delegate;

  public ForwardingJQPlotDataAdapter(IJQPlotDataAdapter<S, K, V, TK> delegate) {
    super(delegate);
    this.delegate = delegate;
  }

  @Override
  protected Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> getObject(
      Locale locale) {
    return transformOnGet(delegate.getObject(), locale);
  }

  protected Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>>
      transformOnGet(
          Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> delegateObject,
          Locale locale) {
    return delegateObject;
  }

  @Override
  public final void setObject(
      Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> object) {
    delegate.setObject(transformOnSet(object));
  }

  protected Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>>
      transformOnSet(
          Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> setObject) {
    return setObject;
  }

  @Override
  public void configure(
      PlotOptions options,
      Map<? extends S, PlotSeries> seriesMap,
      Map<? extends K, PlotTick> keysMap,
      Locale locale) {
    delegate.configure(options, seriesMap, keysMap, locale);
  }

  @Override
  public void detach() {
    delegate.detach();
  }

  @Override
  public void afterConfigure(
      PlotOptions options,
      Map<? extends S, PlotSeries> seriesMap,
      Map<? extends K, PlotTick> keysMap,
      Locale locale) {
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
  public IWrapModel<Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>>>
      wrapOnAssignment(Component component) {
    return new WrapModel(component);
  }

  private class WrapModel
      implements IWrapModel<
          Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>>> {

    private static final long serialVersionUID = 6641833381429869435L;

    private final Component component;

    private final IModel<Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>>>
        wrappedDelegate;

    public WrapModel(Component component) {
      super();
      this.component = component;
      this.wrappedDelegate = delegate.wrapOnAssignment(component);
    }

    @Override
    public Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> getObject() {
      return ForwardingJQPlotDataAdapter.this.transformOnGet(
          wrappedDelegate.getObject(), component.getLocale());
    }

    @Override
    public void setObject(
        Collection<? extends AbstractSeries<TK, V, ? extends SeriesEntry<TK, V>>> object) {
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
