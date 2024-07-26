package org.iglooproject.wicket.more.jqplot.data.provider;

import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractJQPlotDataProvider<S, K, V> implements IJQPlotDataProvider<S, K, V> {

  private static final long serialVersionUID = -5736008017437085384L;

  @Override
  public abstract void detach();

  @Override
  public abstract V getValue(S serie, K key);

  @Override
  public abstract Collection<S> getSeries();

  @Override
  public abstract Collection<K> getKeys();

  @Override
  public Collection<V> getValues() {
    return new ValuesCollection();
  }

  private class ValuesCollection extends AbstractCollection<V> {

    protected V getValue(S series, K key) {
      return AbstractJQPlotDataProvider.this.getValue(series, key);
    }

    protected Collection<? extends S> getSeries() {
      return AbstractJQPlotDataProvider.this.getSeries();
    }

    protected Collection<? extends K> getKeys() {
      return AbstractJQPlotDataProvider.this.getKeys();
    }

    @Override
    public Iterator<V> iterator() {
      return new UnmodifiableIterator<V>() {

        private final Iterator<? extends S> seriesIterator = getSeries().iterator();

        private final boolean hasSerie = seriesIterator.hasNext();

        private S serie = hasSerie ? seriesIterator.next() : null;

        private Iterator<? extends K> keysIterator = getKeys().iterator();

        @Override
        public boolean hasNext() {
          return keysIterator.hasNext() || seriesIterator.hasNext();
        }

        @Override
        public V next() {
          if (hasSerie) {
            if (keysIterator.hasNext()) {
              return getValue(serie, keysIterator.next());
            } else if (seriesIterator.hasNext()) {
              serie = seriesIterator.next();
              keysIterator = getKeys().iterator();
              if (keysIterator.hasNext()) {
                return getValue(serie, keysIterator.next());
              }
            }
          }
          throw new NoSuchElementException();
        }
      };
    }

    @Override
    public int size() {
      return getSeries().size() * getKeys().size();
    }
  }
  ;
}
