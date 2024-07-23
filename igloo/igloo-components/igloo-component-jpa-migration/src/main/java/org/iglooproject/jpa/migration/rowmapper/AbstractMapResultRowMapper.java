package org.iglooproject.jpa.migration.rowmapper;

import java.util.Map;

public abstract class AbstractMapResultRowMapper<K, V> extends AbstractResultRowMapper<Map<K, V>> {

  protected AbstractMapResultRowMapper(Map<K, V> results) {
    super(results);
  }
}
