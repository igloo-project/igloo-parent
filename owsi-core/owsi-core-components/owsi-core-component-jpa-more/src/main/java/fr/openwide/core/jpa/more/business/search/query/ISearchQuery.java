package fr.openwide.core.jpa.more.business.search.query;

import java.util.List;
import java.util.Map;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;
import fr.openwide.core.jpa.query.IQuery;

/**
 * An {@link IQuery} with customizable sort capabilities.
 * @see IQuery
 */
public interface ISearchQuery<T, S extends ISort<?>> extends IQuery<T> {

	ISearchQuery<T, S> sort(Map<S, SortOrder> sortMap);
	
	List<String> listFacetValues(String facetName);
}