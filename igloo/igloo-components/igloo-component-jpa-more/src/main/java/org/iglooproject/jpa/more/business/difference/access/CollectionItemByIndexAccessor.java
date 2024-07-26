package org.iglooproject.jpa.more.business.difference.access;

import com.google.common.collect.Iterables;
import de.danielbechler.diff.access.Accessor;
import de.danielbechler.diff.selector.ElementSelector;
import java.util.Arrays;
import java.util.Collection;
import org.iglooproject.jpa.more.business.difference.selector.CollectionItemByIndexSelector;

public class CollectionItemByIndexAccessor<T> implements Accessor, IKeyAwareAccessor<Integer> {

  private final int index;

  public CollectionItemByIndexAccessor(int index) {
    this.index = index;
  }

  @Override
  public Integer getKey() {
    return index;
  }

  @SuppressWarnings("unchecked")
  private static <T> Collection<T> objectAsCollection(final Object object) {
    if (object == null) {
      return null; // NOSONAR
    } else if (object instanceof Collection) {
      return (Collection<T>) object;
    } else if (object instanceof Object[]) {
      return Arrays.asList((T[]) object);
    }
    throw new IllegalArgumentException(object.getClass().toString());
  }

  @Override
  public ElementSelector getElementSelector() {
    return new CollectionItemByIndexSelector(index);
  }

  @Override
  public void set(final Object target, final Object value) {
    throw new UnsupportedOperationException("Cannot set value on collection/array items.");
  }

  @Override
  public Object get(final Object target) {
    final Collection<T> targetCollection = objectAsCollection(target);
    if (targetCollection == null) {
      return null;
    }
    return Iterables.get(targetCollection, index, null);
  }

  @Override
  public void unset(final Object target) {
    throw new UnsupportedOperationException("Cannot unset value on collection/array items.");
  }

  @Override
  public String toString() {
    return "collection/array item " + getElementSelector();
  }
}
