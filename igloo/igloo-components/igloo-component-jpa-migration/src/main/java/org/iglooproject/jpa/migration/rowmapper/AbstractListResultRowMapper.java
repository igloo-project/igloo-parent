package org.iglooproject.jpa.migration.rowmapper;

import java.util.List;

public abstract class AbstractListResultRowMapper<E> extends AbstractResultRowMapper<List<E>> {

  protected AbstractListResultRowMapper(List<E> results) {
    super(results);
  }
}
