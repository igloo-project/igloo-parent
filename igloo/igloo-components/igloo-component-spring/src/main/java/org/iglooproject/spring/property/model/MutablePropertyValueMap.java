package org.iglooproject.spring.property.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MutablePropertyValueMap implements IMutablePropertyValueMap {

  private Map<MutablePropertyId<?>, Object> delegate = new LinkedHashMap<>();

  @Override
  public Iterator<Entry<?>> iterator() {
    return delegate.entrySet().stream().<Entry<?>>map(input -> new EntryImpl<>(input)).iterator();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T get(MutablePropertyId<T> key) {
    return (T) delegate.get(key);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T put(MutablePropertyId<T> key, T value) {
    return (T) delegate.put(key, value);
  }

  @SuppressWarnings("unchecked")
  private static final class EntryImpl<T> implements Entry<T> {

    private final Map.Entry<MutablePropertyId<?>, Object> delegate;

    protected EntryImpl(Map.Entry<MutablePropertyId<?>, Object> delegate) {
      super();
      this.delegate = delegate;
    }

    @Override
    public MutablePropertyId<T> getKey() {
      return (MutablePropertyId<T>) delegate.getKey();
    }

    @Override
    public T getValue() {
      return (T) delegate.getValue();
    }
  }
}
