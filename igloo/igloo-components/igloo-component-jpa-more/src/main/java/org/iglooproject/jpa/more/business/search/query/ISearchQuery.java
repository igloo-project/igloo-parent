package org.iglooproject.jpa.more.business.search.query;

import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.jpa.query.IQuery;

/**
 * An {@link IQuery} with customizable sort capabilities.
 *
 * @see IQuery
 */
public interface ISearchQuery<T, S extends ISort<?>> extends IQuery<T> {

  ISearchQuery<T, S> sort(Map<S, SortOrder> sortMap);
}
