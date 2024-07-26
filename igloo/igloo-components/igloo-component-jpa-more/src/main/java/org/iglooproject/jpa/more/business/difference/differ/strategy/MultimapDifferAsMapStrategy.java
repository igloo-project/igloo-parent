package org.iglooproject.jpa.more.business.difference.differ.strategy;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import de.danielbechler.diff.access.Accessor;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.iglooproject.jpa.more.business.difference.access.MultimapValueByKeyAccessor;

public class MultimapDifferAsMapStrategy<K, V>
    extends AbstractContainerDifferStrategy<Map<K, Collection<V>>, K> {

  public MultimapDifferAsMapStrategy(ItemContentComparisonStrategy itemContentComparisonStrategy) {
    super(itemContentComparisonStrategy);
  }

  @Override
  protected Accessor createItemAccessor(K key) {
    return new MultimapValueByKeyAccessor<K>(key);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Map<K, Collection<V>> toContainer(Object object) {
    if (object == null) {
      return null;
    } else if (object instanceof Multimap) {
      return ((Multimap) object).asMap();
    } else {
      throw new IllegalArgumentException("This differ only supports Maps and Multimaps.");
    }
  }

  @Override
  public Set<K> difference(Map<K, Collection<V>> source, Map<K, Collection<V>> filter) {
    final Set<K> copy = Sets.newLinkedHashSet();
    if (source != null) {
      if (filter != null) {
        copy.addAll(Sets.difference(source.keySet(), filter.keySet()));
      } else {
        copy.addAll(source.keySet());
      }
    }
    return copy;
  }

  @Override
  public Set<K> intersection(Map<K, Collection<V>> source, Map<K, Collection<V>> filter) {
    final Set<K> copy = Sets.newLinkedHashSet();
    if (source != null && filter != null) {
      copy.addAll(Sets.intersection(source.keySet(), filter.keySet()));
    }
    return copy;
  }
}
