package org.iglooproject.jpa.migration.rowmapper;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import java.util.List;
import java.util.Map;

public abstract class AbstractListMapResultRowMapper<K, V>
    extends AbstractResultRowMapper<Map<K, List<V>>> {

  private final ListMultimap<K, V> multimap;

  protected AbstractListMapResultRowMapper(ListMultimap<K, V> results) {
    this(results, Multimaps.asMap(results));
  }

  private AbstractListMapResultRowMapper(ListMultimap<K, V> multimap, Map<K, List<V>> mapView) {
    super(mapView);
    this.multimap = multimap;
  }

  protected ListMultimap<K, V> getMultimap() {
    return multimap;
  }
}
