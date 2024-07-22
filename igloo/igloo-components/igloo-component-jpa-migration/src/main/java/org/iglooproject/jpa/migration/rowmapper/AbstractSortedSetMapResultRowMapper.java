package org.iglooproject.jpa.migration.rowmapper;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SortedSetMultimap;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import org.iglooproject.functional.Suppliers2;

public abstract class AbstractSortedSetMapResultRowMapper<K, V>
    extends AbstractResultRowMapper<Map<K, SortedSet<V>>> {

  private final SortedSetMultimap<K, V> multimap;

  protected static <K, V> SortedSetMultimap<K, V> newTreeSetHashMultimap(
      int expectedKeys, int expectedValuesPerKey, Comparator<? super V> comparator) {
    Map<K, Collection<V>> map = Maps.newHashMapWithExpectedSize(expectedKeys);
    return Multimaps.newSortedSetMultimap(map, Suppliers2.treeSet(comparator));
  }

  protected AbstractSortedSetMapResultRowMapper(SortedSetMultimap<K, V> results) {
    this(results, Multimaps.asMap(results));
  }

  private AbstractSortedSetMapResultRowMapper(
      SortedSetMultimap<K, V> multimap, Map<K, SortedSet<V>> mapView) {
    super(mapView);
    this.multimap = multimap;
  }

  protected SortedSetMultimap<K, V> getMultimapResults() {
    return multimap;
  }
}
