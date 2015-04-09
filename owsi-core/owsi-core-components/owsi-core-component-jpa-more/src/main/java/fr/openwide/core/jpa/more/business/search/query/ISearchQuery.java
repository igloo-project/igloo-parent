package fr.openwide.core.jpa.more.business.search.query;

import java.util.List;
import java.util.Map;

import org.apache.lucene.search.SortField;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.ISort.SortOrder;

public interface ISearchQuery<T, S extends ISort<SortField>> {
	
	List<T> fullList();
	
	List<T> list(long offset, long limit);

	long count();

	ISearchQuery<T, S> sort(Map<S, SortOrder> sortMap);
}