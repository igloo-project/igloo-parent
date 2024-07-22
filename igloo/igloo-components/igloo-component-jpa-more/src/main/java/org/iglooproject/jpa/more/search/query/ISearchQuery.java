package org.iglooproject.jpa.more.search.query;

import java.util.Collection;
import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface ISearchQuery<T, S extends ISort<?>, D extends ISearchQueryData<? super T>> {

  Collection<T> list(D data, Map<S, ISort.SortOrder> sorts, Integer offset, Integer limit);

  long size(D data);

  default boolean checkLimit(Integer limit) {
    return limit == null || limit > 0;
  }
}
