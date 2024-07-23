package org.iglooproject.jpa.more.business.difference.differ.strategy;

import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractCollectionDifferStrategy<T, K>
    extends AbstractContainerDifferStrategy<Collection<T>, K> {

  public AbstractCollectionDifferStrategy(
      ItemContentComparisonStrategy itemContentComparisonStrategy) {
    super(itemContentComparisonStrategy);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<T> toContainer(Object object) {
    if (object == null) {
      return null; // NOSONAR
    } else if (object instanceof Collection) {
      return (Collection<T>) object;
    } else if (object instanceof Object[]) {
      return Arrays.asList((T[]) object);
    } else {
      throw new IllegalArgumentException("This strategy only supports collections and arrays.");
    }
  }
}
