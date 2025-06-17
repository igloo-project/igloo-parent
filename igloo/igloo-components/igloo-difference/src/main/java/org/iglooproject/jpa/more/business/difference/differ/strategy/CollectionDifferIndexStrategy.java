package org.iglooproject.jpa.more.business.difference.differ.strategy;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Iterables;
import com.google.common.collect.Range;
import de.danielbechler.diff.access.Accessor;
import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.more.business.difference.access.CollectionItemByIndexAccessor;

public final class CollectionDifferIndexStrategy<T>
    extends AbstractCollectionDifferStrategy<T, Integer> {
  public CollectionDifferIndexStrategy(
      ItemContentComparisonStrategy itemContentComparisonStrategy) {
    super(itemContentComparisonStrategy);
  }

  @Override
  protected Accessor createItemAccessor(Integer key) {
    return new CollectionItemByIndexAccessor<T>(key);
  }

  @Override
  public Iterable<Integer> difference(Collection<T> source, Collection<T> filter) {
    int sourceSize = source == null ? 0 : Iterables.size(source);
    int filterSize = filter == null ? 0 : Iterables.size(filter);
    if (sourceSize > filterSize) {
      return ContiguousSet.create(
          Range.closedOpen(filterSize, sourceSize), DiscreteDomain.integers());
    } else {
      return List.of();
    }
  }

  @Override
  public Iterable<Integer> intersection(Collection<T> source, Collection<T> filter) {
    int sourceSize = source == null ? 0 : Iterables.size(source);
    int filterSize = filter == null ? 0 : Iterables.size(filter);
    return ContiguousSet.create(
        Range.closedOpen(0, Math.min(sourceSize, filterSize)), DiscreteDomain.integers());
  }
}
