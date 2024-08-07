package org.iglooproject.jpa.more.business.search.query;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;

public abstract class AbstractSearchQuery<T, S extends ISort<?>>
    implements ISearchQuery<T, S> /* NOT Serializable */ {

  @PersistenceContext protected EntityManager entityManager;

  protected List<S> defaultSorts;
  protected Map<S, SortOrder> sortMap;

  @SafeVarargs
  protected AbstractSearchQuery(S... defaultSorts) {
    this.defaultSorts = ImmutableList.copyOf(defaultSorts);
  }

  // Sort
  @Override
  public ISearchQuery<T, S> sort(Map<S, SortOrder> sortMap) {
    this.sortMap = ImmutableMap.copyOf(sortMap);
    return this;
  }
}
